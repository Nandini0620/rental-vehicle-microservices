import { today } from './format.js'

const EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

/** Mirrors backend annotations. Returns an error message or ''. `orig` = original value in edit mode. */
export function validateField(f, v, orig) {
  const s = v === null || v === undefined ? '' : String(v).trim()
  if (f.required && s === '') return `${f.label} is required`
  if (s === '') return ''
  if (f.type === 'email' && !EMAIL.test(s)) return 'Email format is invalid'
  if (f.pattern && !f.pattern.test(s)) return f.patternMsg || `${f.label} is invalid`
  if (f.minLen && s.length < f.minLen) return `${f.label} must be at least ${f.minLen} characters`
  if (f.maxLen && s.length > f.maxLen) return `${f.label} cannot exceed ${f.maxLen} characters`
  if (f.type === 'number' || f.num) {
    const n = Number(s)
    if (Number.isNaN(n)) return `${f.label} must be a number`
    if (f.int && !Number.isInteger(n)) return `${f.label} must be a whole number`
    if (f.gt !== undefined && !(n > f.gt)) return f.gtMsg || `${f.label} must be greater than ${f.gt}`
    if (f.min !== undefined && n < f.min) return f.minMsg || `${f.label} must be at least ${f.min}`
    if (f.max !== undefined && n > f.max) return f.maxMsg || `${f.label} cannot exceed ${f.max}`
  }
  if (f.future && s < today() && s !== orig) return `${f.label} cannot be in the past`
  return ''
}
