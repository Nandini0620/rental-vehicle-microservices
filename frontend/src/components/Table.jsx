import { Badge, EmptyState, Loading } from './ui.jsx'
import { cellValue } from '../config/resources.js'

export default function Table({ cols, rows, refs, loading, onEdit, onDelete, emptyText }) {
  if (loading) return <Loading />
  if (!rows.length) return <EmptyState text={emptyText} />
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full text-sm">
        <thead className="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
          <tr>
            {cols.map((c) => <th key={c.key + c.label} className="whitespace-nowrap px-4 py-3">{c.label}</th>)}
            <th className="px-4 py-3 text-right">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100">
          {rows.map((r) => (
            <tr key={r.id} className="hover:bg-slate-50">
              {cols.map((c) => (
                <td key={c.key + c.label} className={`px-4 py-3 ${c.clip ? 'max-w-[16rem] truncate' : 'whitespace-nowrap'}`} title={c.clip ? r[c.key] : undefined}>
                  {c.type === 'badge' ? <Badge value={r[c.key]} /> : cellValue(c, r, refs)}
                </td>
              ))}
              <td className="whitespace-nowrap px-4 py-3 text-right">
                <button onClick={() => onEdit(r)} className="mr-3 font-medium text-indigo-600 hover:underline">Edit</button>
                <button onClick={() => onDelete(r)} className="font-medium text-red-600 hover:underline">Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
