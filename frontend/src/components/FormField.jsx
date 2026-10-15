/** Renders label + control (+ error) for a field config. `options` = [{value,label}] for selects. */
export default function FormField({ field, value, onChange, error, disabled, options = [] }) {
  const cls = `w-full rounded-lg border px-3 py-2 text-sm outline-none focus:ring-2 focus:ring-indigo-500 disabled:cursor-not-allowed disabled:bg-slate-100 disabled:text-slate-500 ${error ? 'border-red-400' : 'border-slate-300'}`
  const common = { id: field.name, disabled, value: value ?? '', onChange: (e) => onChange(e.target.value), className: cls }
  let ctl
  if (field.type === 'select')
    ctl = (
      <select {...common}>
        <option value="">Select {field.label.toLowerCase()}…</option>
        {options.map((o) => <option key={o.value} value={o.value}>{o.label}</option>)}
      </select>
    )
  else if (field.type === 'textarea') ctl = <textarea rows={3} {...common} />
  else
    ctl = (
      <input {...common} type={field.type === 'number' ? 'number' : field.type === 'date' ? 'date' : 'text'}
        step={field.step} placeholder={field.placeholder} inputMode={field.type === 'email' ? 'email' : undefined} />
    )
  return (
    <div>
      <label htmlFor={field.name} className="mb-1 block text-sm font-medium text-slate-700">
        {field.label}{field.required && <span className="text-red-500"> *</span>}
        {disabled && <span className="ml-2 text-xs font-normal text-slate-400">(locked)</span>}
      </label>
      {ctl}
      {error && <p className="mt-1 text-xs text-red-600">{error}</p>}
    </div>
  )
}
