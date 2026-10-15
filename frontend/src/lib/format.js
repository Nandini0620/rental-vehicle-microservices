const pad = (n) => String(n).padStart(2, '0')
export const today = () => {
  const d = new Date()
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
}
export const fmtDate = (d) => {
  if (!d) return '—'
  if (Array.isArray(d)) return `${d[0]}-${pad(d[1])}-${pad(d[2])}`
  return String(d).slice(0, 10)
}
export const fmtMoney = (n) =>
  n === null || n === undefined || n === '' ? '—' : '₹' + Number(n).toLocaleString('en-IN', { maximumFractionDigits: 2 })

export const BADGE = {
  PENDING: 'bg-amber-100 text-amber-800', CONFIRMED: 'bg-blue-100 text-blue-800', COMPLETED: 'bg-green-100 text-green-800',
  CANCELLED: 'bg-red-100 text-red-800', SUCCESS: 'bg-green-100 text-green-800', FAILED: 'bg-red-100 text-red-800',
  REFUNDED: 'bg-purple-100 text-purple-800', SCHEDULED: 'bg-blue-100 text-blue-800',
  BIKE: 'bg-teal-100 text-teal-800', CAR: 'bg-indigo-100 text-indigo-800', SUV: 'bg-orange-100 text-orange-800',
  BUS: 'bg-sky-100 text-sky-800', TRUCK: 'bg-stone-200 text-stone-800',
}
