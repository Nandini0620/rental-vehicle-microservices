import { fmtDate, fmtMoney } from '../lib/format.js'

const find = (refs, k, id) => refs?.[k]?.find((x) => x.id === id)
export const LABEL = {
  customers: (r) => r.name,
  vehicles: (r) => r.vehicleName,
  drivers: (r) => r.name,
  trips: (r) => r.title,
  bookings: (r, refs) => `#${r.id} · ${nameOf(refs, 'customers', r.customerId)} · ${nameOf(refs, 'vehicles', r.vehicleId)} · ${fmtDate(r.bookingDate)}`,
  payments: (r) => `#${r.id}`,
  feedbacks: (r) => `#${r.id}`,
}
export const nameOf = (refs, k, id) => {
  const r = find(refs, k, id)
  return r ? LABEL[k](r, refs) : `#${id}`
}

/** Display text for a table cell (also used for search). */
export function cellValue(c, r, refs) {
  const v = r[c.key]
  switch (c.type) {
    case 'ref': return nameOf(refs, c.ref, v)
    case 'date': return fmtDate(v)
    case 'money': return fmtMoney(v)
    case 'stars': return '★'.repeat(v || 0) + '☆'.repeat(5 - (v || 0))
    default: return v === null || v === undefined || v === '' ? '—' : String(v)
  }
}

const TYPES = ['BIKE', 'CAR', 'SUV', 'BUS', 'TRUCK']
const sameCustomer = (r, form) => !form.customerId || String(r.customerId) === String(form.customerId)
const id = { key: 'id', label: 'ID' }

// field: {name,label,type(text|email|number|date|select|textarea),required,ref,options,lockOnEdit,...validation}
// cols : {key,label,type(ref|date|money|badge|stars),ref,clip}
export const RESOURCES = {
  customers: {
    key: 'customers', title: 'Customers', singular: 'Customer', needs: [],
    fields: [
      { name: 'name', label: 'Name', required: true },
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'mobile', label: 'Mobile', required: true, pattern: /^[0-9]{10}$/, patternMsg: 'Mobile must be 10 digits' },
    ],
    cols: [id, { key: 'name', label: 'Name' }, { key: 'email', label: 'Email' }, { key: 'mobile', label: 'Mobile' }],
  },
  vehicles: {
    key: 'vehicles', title: 'Vehicles', singular: 'Vehicle', needs: [],
    fields: [
      { name: 'vehicleName', label: 'Vehicle name', required: true, maxLen: 100 },
      { name: 'model', label: 'Model', required: true },
      { name: 'type', label: 'Type', type: 'select', options: TYPES, required: true },
      { name: 'dailyFee', label: 'Daily fee (₹)', type: 'number', step: '0.01', required: true, gt: 0, gtMsg: 'Daily fee must be greater than 0' },
    ],
    cols: [id, { key: 'vehicleName', label: 'Name' }, { key: 'model', label: 'Model' }, { key: 'type', label: 'Type', type: 'badge' }, { key: 'dailyFee', label: 'Daily fee', type: 'money' }],
  },
  drivers: {
    key: 'drivers', title: 'Drivers', singular: 'Driver', needs: [],
    fields: [
      { name: 'name', label: 'Name', required: true },
      { name: 'email', label: 'Email', type: 'email', required: true },
      { name: 'licenseNumber', label: 'License number', required: true, minLen: 8, maxLen: 20 },
      { name: 'specialization', label: 'Specialization', type: 'select', options: TYPES, required: true },
    ],
    cols: [id, { key: 'name', label: 'Name' }, { key: 'email', label: 'Email' }, { key: 'licenseNumber', label: 'License' }, { key: 'specialization', label: 'Specialization', type: 'badge' }],
  },
  bookings: {
    key: 'bookings', title: 'Bookings', singular: 'Booking', needs: ['customers', 'vehicles'],
    fields: [
      { name: 'customerId', label: 'Customer', type: 'select', ref: 'customers', required: true, lockOnEdit: true },
      { name: 'vehicleId', label: 'Vehicle', type: 'select', ref: 'vehicles', required: true, lockOnEdit: true },
      { name: 'bookingDate', label: 'Booking date', type: 'date', required: true, future: true },
      { name: 'durationDays', label: 'Duration (days)', type: 'number', int: true, required: true, min: 1, max: 30, minMsg: 'Duration must be at least 1 day', maxMsg: 'Duration cannot exceed 30 days' },
    ],
    cols: [id, { key: 'customerId', label: 'Customer', type: 'ref', ref: 'customers' }, { key: 'vehicleId', label: 'Vehicle', type: 'ref', ref: 'vehicles' },
      { key: 'bookingDate', label: 'Date', type: 'date' }, { key: 'durationDays', label: 'Days' }, { key: 'status', label: 'Status', type: 'badge' }],
  },
  payments: {
    key: 'payments', title: 'Payments', singular: 'Payment', needs: ['customers', 'bookings', 'vehicles'],
    fields: [
      { name: 'customerId', label: 'Customer', type: 'select', ref: 'customers', required: true },
      { name: 'bookingId', label: 'Booking', type: 'select', ref: 'bookings', required: true, filter: sameCustomer },
      { name: 'amount', label: 'Amount (₹)', type: 'number', step: '0.01', required: true, gt: 0, max: 1000000, gtMsg: 'Amount must be greater than 0', maxMsg: 'Amount cannot exceed 10,00,000' },
    ],
    // picking a booking fills its customer and (if empty) a suggested amount = days × daily fee
    derive: (form, name, refs) => {
      if (name === 'bookingId') {
        const b = find(refs, 'bookings', Number(form.bookingId))
        if (!b) return {}
        const v = find(refs, 'vehicles', b.vehicleId)
        const patch = { customerId: String(b.customerId) }
        if (form.amount === '' && v) patch.amount = String(b.durationDays * v.dailyFee)
        return patch
      }
      if (name === 'customerId' && form.bookingId) {
        const b = find(refs, 'bookings', Number(form.bookingId))
        if (b && String(b.customerId) !== String(form.customerId)) return { bookingId: '' }
      }
      return {}
    },
    cols: [id, { key: 'customerId', label: 'Customer', type: 'ref', ref: 'customers' }, { key: 'bookingId', label: 'Booking', type: 'ref', ref: 'bookings' },
      { key: 'amount', label: 'Amount', type: 'money' }, { key: 'paymentDate', label: 'Paid on', type: 'date' }, { key: 'status', label: 'Status', type: 'badge' }],
  },
  trips: {
    key: 'trips', title: 'Trips', singular: 'Trip', needs: ['vehicles', 'drivers'],
    fields: [
      { name: 'title', label: 'Title', required: true, maxLen: 100 },
      { name: 'routeDetails', label: 'Route details', type: 'textarea', required: true, maxLen: 500 },
      { name: 'vehicleId', label: 'Vehicle', type: 'select', ref: 'vehicles', required: true, lockOnEdit: true },
      { name: 'driverId', label: 'Driver', type: 'select', ref: 'drivers', required: true, lockOnEdit: true },
      { name: 'dueDate', label: 'Due date', type: 'date', required: true, future: true },
    ],
    cols: [id, { key: 'title', label: 'Title' }, { key: 'routeDetails', label: 'Route', clip: true }, { key: 'vehicleId', label: 'Vehicle', type: 'ref', ref: 'vehicles' },
      { key: 'driverId', label: 'Driver', type: 'ref', ref: 'drivers' }, { key: 'dueDate', label: 'Due', type: 'date' }, { key: 'status', label: 'Status', type: 'badge' }],
  },
  feedbacks: {
    key: 'feedbacks', title: 'Feedback', singular: 'Feedback', needs: ['customers', 'trips', 'bookings', 'vehicles'],
    fields: [
      { name: 'customerId', label: 'Customer', type: 'select', ref: 'customers', required: true, lockOnEdit: true },
      { name: 'tripId', label: 'Trip', type: 'select', ref: 'trips', required: true, lockOnEdit: true },
      { name: 'bookingId', label: 'Booking', type: 'select', ref: 'bookings', required: true, lockOnEdit: true, filter: sameCustomer },
      { name: 'ratingScore', label: 'Rating', type: 'select', options: [1, 2, 3, 4, 5], num: true, required: true },
      { name: 'comments', label: 'Comments', type: 'textarea', maxLen: 500 },
    ],
    derive: (form, name, refs) => {
      if (name === 'bookingId') {
        const b = find(refs, 'bookings', Number(form.bookingId))
        return b ? { customerId: String(b.customerId) } : {}
      }
      if (name === 'customerId' && form.bookingId) {
        const b = find(refs, 'bookings', Number(form.bookingId))
        if (b && String(b.customerId) !== String(form.customerId)) return { bookingId: '' }
      }
      return {}
    },
    cols: [id, { key: 'customerId', label: 'Customer', type: 'ref', ref: 'customers' }, { key: 'tripId', label: 'Trip', type: 'ref', ref: 'trips' },
      { key: 'bookingId', label: 'Booking', type: 'ref', ref: 'bookings' }, { key: 'ratingScore', label: 'Rating', type: 'stars' },
      { key: 'comments', label: 'Comments', clip: true }, { key: 'feedbackDate', label: 'Date', type: 'date' }],
  },
}
