import { createContext, useCallback, useContext, useState } from 'react'

const Ctx = createContext(null)
export const useToast = () => useContext(Ctx)

export function ToastProvider({ children }) {
  const [items, setItems] = useState([])
  const push = useCallback((type, text) => {
    const id = Math.random()
    setItems((l) => [...l, { id, type, text }])
    setTimeout(() => setItems((l) => l.filter((t) => t.id !== id)), 4500)
  }, [])
  const api = { success: (t) => push('success', t), error: (t) => push('error', t) }
  return (
    <Ctx.Provider value={api}>
      {children}
      <div className="fixed top-4 right-4 z-[60] flex w-80 max-w-[calc(100vw-2rem)] flex-col gap-2">
        {items.map((t) => (
          <div key={t.id} role="status"
            className={`rounded-lg px-4 py-3 text-sm text-white shadow-lg ${t.type === 'success' ? 'bg-emerald-600' : 'bg-red-600'}`}>
            {t.text}
          </div>
        ))}
      </div>
    </Ctx.Provider>
  )
}
