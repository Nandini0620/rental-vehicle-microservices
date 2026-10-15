import { useEffect } from 'react'

export default function Modal({ title, onClose, children, width = 'max-w-lg' }) {
  useEffect(() => {
    const h = (e) => e.key === 'Escape' && onClose()
    window.addEventListener('keydown', h)
    return () => window.removeEventListener('keydown', h)
  }, [onClose])
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/50 p-4" onMouseDown={(e) => e.target === e.currentTarget && onClose()}>
      <div className={`max-h-[90vh] w-full ${width} overflow-y-auto rounded-xl bg-white shadow-xl`}>
        <div className="flex items-center justify-between border-b px-5 py-3">
          <h2 className="text-lg font-semibold">{title}</h2>
          <button onClick={onClose} aria-label="Close" className="text-2xl leading-none text-slate-400 hover:text-slate-700">×</button>
        </div>
        <div className="p-5">{children}</div>
      </div>
    </div>
  )
}
