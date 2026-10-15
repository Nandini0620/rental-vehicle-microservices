import { BADGE } from '../lib/format.js'

export const Spinner = ({ className = 'h-8 w-8' }) => (
  <div className={`animate-spin rounded-full border-4 border-indigo-200 border-t-indigo-600 ${className}`} />
)
export const Loading = () => <div className="flex justify-center py-16"><Spinner /></div>
export const Badge = ({ value }) =>
  value ? <span className={`inline-block rounded-full px-2.5 py-0.5 text-xs font-medium ${BADGE[value] || 'bg-slate-100 text-slate-700'}`}>{value}</span> : '—'
export const EmptyState = ({ text = 'No records yet', children }) => (
  <div className="py-16 text-center text-slate-500"><div className="mb-2 text-4xl">📭</div><p>{text}</p>{children}</div>
)
export const Btn = ({ variant = 'primary', className = '', ...p }) => (
  <button {...p} className={`rounded-lg px-4 py-2 text-sm font-medium transition disabled:opacity-50 ${
    variant === 'primary' ? 'bg-indigo-600 text-white hover:bg-indigo-700'
    : variant === 'danger' ? 'bg-red-600 text-white hover:bg-red-700'
    : 'border border-slate-300 bg-white text-slate-700 hover:bg-slate-50'} ${className}`} />
)
