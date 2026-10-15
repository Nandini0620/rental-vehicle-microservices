import axios from 'axios'

const base = import.meta.env.DEV ? '/api' : import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
export const http = axios.create({ baseURL: base, timeout: 15000 })

// Gateway path prefixes (from api-gateway application.properties) + per-service endpoint shapes.
export const ENDPOINTS = {
  customers: { path: '/customers', list: '/get' },
  vehicles: { path: '/vehicles', list: '/get' },
  drivers: { path: '/drivers', list: '/get' },
  bookings: { path: '/bookings', list: '/get' },
  payments: { path: '/payments', list: '/get' },
  trips: { path: '/trips', list: '/get' },
  feedbacks: { path: '/feedbacks', list: '' }, // GET /feedbacks (no /get)
}

export const api = {
  list: (k) => http.get(ENDPOINTS[k].path + ENDPOINTS[k].list).then((r) => r.data),
  create: (k, body) => http.post(`${ENDPOINTS[k].path}/create`, body).then((r) => r.data),
  update: (k, id, body) => http.put(`${ENDPOINTS[k].path}/update/${id}`, body).then((r) => r.data),
  remove: (k, id) => http.delete(`${ENDPOINTS[k].path}/delete/${id}`).then((r) => r.data),
}

/**
 * Normalises backend errors. Backend body: {timestamp,status,error,message}.
 * Validation (customer/driver) arrives as message "{field=Msg, field2=Msg}" -> parsed to fields.
 * Spring default errors ({errors:[{field,defaultMessage}]}) are handled too.
 */
export function parseError(err) {
  if (!err || !err.response) return { status: 0, message: 'Cannot reach server', fields: {} }
  const { status, data } = err.response
  const fields = {}
  if (status === 503) return { status, message: 'Service temporarily unavailable', fields }
  const raw = (data && typeof data === 'object' ? data.message : typeof data === 'string' ? data : '') || ''
  if (status === 400) {
    if (data && Array.isArray(data.errors)) data.errors.forEach((e) => e.field && (fields[e.field] = e.defaultMessage || e.message))
    const m = /^\{([\s\S]*)\}$/.exec(raw.trim())
    if (m) {
      m[1].split(/,\s(?=[A-Za-z_][\w.]*=)/).forEach((p) => {
        const i = p.indexOf('=')
        if (i > 0) fields[p.slice(0, i).trim()] = p.slice(i + 1).trim()
      })
    }
  }
  const fallback = status === 404 ? 'Not found' : status === 409 ? 'Conflict: duplicate or invalid data' : `Request failed (${status})`
  return { status, message: raw || (data && data.error) || fallback, fields }
}
