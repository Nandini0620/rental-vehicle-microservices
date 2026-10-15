import { useCallback, useEffect, useMemo, useState } from 'react'
import { api, parseError } from '../api/client.js'
import { cellValue, LABEL, nameOf } from '../config/resources.js'
import { validateField } from '../lib/validate.js'
import { fmtDate } from '../lib/format.js'
import ConfirmDialog from './ConfirmDialog.jsx'
import FormField from './FormField.jsx'
import Modal from './Modal.jsx'
import Table from './Table.jsx'
import { useToast } from './Toast.jsx'
import { Btn } from './ui.jsx'

const isNum = (f) => f.type === 'number' || f.ref || f.num
const toForm = (cfg, row) =>
  Object.fromEntries(cfg.fields.map((f) => {
    const v = row ? row[f.name] : ''
    return [f.name, v === null || v === undefined ? '' : f.type === 'date' ? fmtDate(v) : String(v)]
  }))
// Only the fields in the config are sent -> server-set fields (status, dates, id) are never included.
const toPayload = (cfg, form) =>
  Object.fromEntries(cfg.fields.map((f) => [f.name, isNum(f) ? (form[f.name] === '' ? null : Number(form[f.name])) : String(form[f.name]).trim()]))

function EntityForm({ cfg, row, refs, onClose, onSaved }) {
  const toast = useToast()
  const edit = !!row
  const orig = useMemo(() => toForm(cfg, row), [cfg, row])
  const [form, setForm] = useState(orig)
  const [errors, setErrors] = useState({})
  const [banner, setBanner] = useState('')
  const [saving, setSaving] = useState(false)

  const set = (name, value) => {
    setForm((f) => {
      const next = { ...f, [name]: value }
      return cfg.derive ? { ...next, ...cfg.derive(next, name, refs) } : next
    })
    setErrors((e) => ({ ...e, [name]: '' }))
  }
  const options = (f) =>
    f.ref
      ? (refs[f.ref] || []).filter((r) => !f.filter || f.filter(r, form)).map((r) => ({ value: r.id, label: LABEL[f.ref](r, refs) }))
      : (f.options || []).map((o) => ({ value: o, label: String(o) }))

  const submit = async (e) => {
    e.preventDefault()
    const errs = {}
    cfg.fields.forEach((f) => { const m = validateField(f, form[f.name], orig[f.name]); if (m) errs[f.name] = m })
    setErrors(errs)
    if (Object.keys(errs).length) { setBanner('Please fix the highlighted fields.'); return }
    setBanner(''); setSaving(true)
    try {
      const body = toPayload(cfg, form)
      if (edit) await api.update(cfg.key, row.id, body)
      else await api.create(cfg.key, body)
      toast.success(`${cfg.singular} ${edit ? 'updated' : 'created'}`)
      onSaved()
    } catch (err) {
      const { message, fields } = parseError(err)
      const known = {}, rest = []
      Object.entries(fields).forEach(([k, v]) => (cfg.fields.some((f) => f.name === k) ? (known[k] = v) : rest.push(`${k}: ${v}`)))
      setErrors(known)
      setBanner(Object.keys(known).length ? ['Validation failed.', ...rest].join(' ') : message)
      if (!Object.keys(known).length) toast.error(message)
    } finally { setSaving(false) }
  }

  return (
    <Modal title={`${edit ? 'Edit' : 'Add'} ${cfg.singular}`} onClose={onClose}>
      <form onSubmit={submit} noValidate className="space-y-4">
        {banner && <div className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{banner}</div>}
        {cfg.fields.map((f) => (
          <FormField key={f.name} field={f} value={form[f.name]} onChange={(v) => set(f.name, v)}
            error={errors[f.name]} disabled={edit && f.lockOnEdit} options={f.type === 'select' ? options(f) : []} />
        ))}
        <div className="flex justify-end gap-2 pt-2">
          <Btn type="button" variant="ghost" onClick={onClose} disabled={saving}>Cancel</Btn>
          <Btn type="submit" disabled={saving}>{saving ? 'Saving…' : edit ? 'Save changes' : 'Create'}</Btn>
        </div>
      </form>
    </Modal>
  )
}

/** Generic CRUD page: table + search + modal form + delete confirm, driven by a config from resources.js. */
export default function ResourcePage({ cfg }) {
  const toast = useToast()
  const [rows, setRows] = useState([])
  const [refs, setRefs] = useState({})
  const [loading, setLoading] = useState(true)
  const [loadErr, setLoadErr] = useState('')
  const [q, setQ] = useState('')
  const [modal, setModal] = useState(null) // { row? }
  const [del, setDel] = useState(null)
  const [deleting, setDeleting] = useState(false)

  const load = useCallback(async () => {
    setLoading(true); setLoadErr('')
    const keys = [cfg.key, ...cfg.needs]
    const res = await Promise.allSettled(keys.map((k) => api.list(k)))
    if (res[0].status === 'rejected') setLoadErr(parseError(res[0].reason).message)
    else setRows([...res[0].value].sort((a, b) => a.id - b.id))
    const r = {}, failed = []
    keys.slice(1).forEach((k, i) => (res[i + 1].status === 'fulfilled' ? (r[k] = res[i + 1].value) : failed.push(k)))
    setRefs(r)
    if (failed.length) toast.error(`Could not load ${failed.join(', ')} (dropdowns/names may be incomplete)`)
    setLoading(false)
  }, [cfg]) // eslint-disable-line react-hooks/exhaustive-deps
  useEffect(() => { load() }, [load])
  useEffect(() => { setQ('') }, [cfg])

  const shown = useMemo(() => {
    const s = q.trim().toLowerCase()
    return s ? rows.filter((r) => cfg.cols.some((c) => cellValue(c, r, refs).toLowerCase().includes(s) || String(r[c.key]).toLowerCase().includes(s))) : rows
  }, [rows, refs, q, cfg])

  const confirmDelete = async () => {
    setDeleting(true)
    try { await api.remove(cfg.key, del.id); toast.success(`${cfg.singular} deleted`); setDel(null); load() }
    catch (e) { toast.error(parseError(e).message); setDel(null) }
    finally { setDeleting(false) }
  }

  return (
    <div>
      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        <h1 className="text-2xl font-bold">{cfg.title}</h1>
        <div className="flex gap-2">
          <input value={q} onChange={(e) => setQ(e.target.value)} placeholder="Search…" className="w-40 rounded-lg border border-slate-300 px-3 py-2 text-sm sm:w-56" />
          <Btn onClick={() => setModal({})}>+ Add {cfg.singular}</Btn>
        </div>
      </div>
      <div className="rounded-xl border border-slate-200 bg-white shadow-sm">
        {loadErr ? (
          <div className="py-12 text-center"><p className="mb-3 text-red-600">{loadErr}</p><Btn variant="ghost" onClick={load}>Retry</Btn></div>
        ) : (
          <Table cols={cfg.cols} rows={shown} refs={refs} loading={loading} onEdit={(row) => setModal({ row })} onDelete={setDel}
            emptyText={q ? 'No matching records' : `No ${cfg.title.toLowerCase()} yet`} />
        )}
      </div>
      {modal && <EntityForm cfg={cfg} row={modal.row} refs={refs} onClose={() => setModal(null)} onSaved={() => { setModal(null); load() }} />}
      {del && <ConfirmDialog busy={deleting} onCancel={() => setDel(null)} onConfirm={confirmDelete}
        message={`Delete ${cfg.singular.toLowerCase()} ${LABEL[cfg.key](del, refs)}? This cannot be undone.`} />}
    </div>
  )
}
