import { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client.js'
import { Badge, Spinner } from '../components/ui.jsx'

const CARDS = [
  ['vehicles', 'Total fleet', '/vehicles', '◇', 'Fleet overview', 'sage'],
  ['bookings', 'Bookings', '/bookings', '▤', 'Reservations made', 'lavender'],
  ['customers', 'Customers', '/customers', '♙', 'People on the move', 'peach'],
  ['drivers', 'Drivers', '/drivers', '♧', 'Driver network', 'butter'],
]
const count = (list, key) => list.reduce((m, r) => ((m[r[key]] = (m[r[key]] || 0) + 1), m), {})
const nice = (v) => String(v || 'Unknown').replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, (c) => c.toUpperCase())

export default function Dashboard() {
  const [data, setData] = useState(null)
  const [now, setNow] = useState(new Date())
  useEffect(() => {
    Promise.allSettled([...new Set([...CARDS.map(([k]) => k), 'trips', 'payments', 'feedbacks'])].map((k) => api.list(k))).then((res) => {
      const keys = [...new Set([...CARDS.map(([k]) => k), 'trips', 'payments', 'feedbacks'])]
      setData(Object.fromEntries(keys.map((k, i) => [k, res[i].status === 'fulfilled' && Array.isArray(res[i].value) ? res[i].value : null])))
    })
    const timer = setInterval(() => setNow(new Date()), 60000)
    return () => clearInterval(timer)
  }, [])
  const total = (k) => data?.[k] ? data[k].length : '—'
  const bookingBreakdown = useMemo(() => data?.bookings ? Object.entries(count(data.bookings, 'status')).sort((a,b) => b[1]-a[1]) : [], [data])
  const recent = useMemo(() => data?.bookings ? [...data.bookings].sort((a,b) => new Date(b.createdAt || b.bookingDate || 0) - new Date(a.createdAt || a.bookingDate || 0)).slice(0,5) : [], [data])
  if (!data) return <div className="loading-screen"><Spinner /><p>Getting your operations ready…</p></div>
  return <div className="dashboard-page">
    <section className="welcome-row"><div><div className="eyebrow"><span className="eyebrow-line"/> YOUR OPERATIONS AT A GLANCE</div><h1>A clearer view of <em>every journey.</em></h1><p className="welcome-copy">Good {now.getHours() < 12 ? 'morning' : now.getHours() < 17 ? 'afternoon' : 'evening'}. Here’s what’s happening across your rental business.</p></div><div className="date-chip"><span className="date-icon">▦</span><div><small>TODAY</small><strong>{now.toLocaleDateString('en-IN', { weekday: 'short', day: 'numeric', month: 'short', year: 'numeric' })}</strong></div></div></section>
    <section className="hero-banner"><div className="hero-content"><div className="hero-kicker"><span/> THE ROAD AHEAD LOOKS GOOD</div><h2>Move people.<br/><i>Make moments.</i></h2><p>All the moving parts, thoughtfully brought together in one place.</p><Link to="/vehicles" className="hero-cta">Explore your fleet <span>↗</span></Link></div><div className="hero-art" aria-hidden="true"><div className="hero-orbit orbit-one"/><div className="hero-orbit orbit-two"/><div className="hero-sun"/><div className="hero-mountain mountain-back"/><div className="hero-mountain mountain-front"/><div className="hero-road"/><div className="hero-car"><div className="car-window"/><div className="car-wheel wheel-left"/><div className="car-wheel wheel-right"/></div><div className="hero-spark spark-one">✦</div><div className="hero-spark spark-two">✳</div></div><div className="hero-index"><strong>01</strong><span> / OPERATIONS</span></div></section>
    <section className="section-heading"><div><div className="eyebrow">THE BIG PICTURE</div><h2>Your business, in numbers</h2></div><span className="section-note"><span className="live-dot"/> Live overview</span></section>
    <section className="stats-grid">{CARDS.map(([k,label,to,icon,sub,tone], i) => <Link key={k} to={to} className={`stat-card stat-${tone}`}><div className="stat-top"><span className="stat-icon">{icon}</span><span className="stat-arrow">↗</span></div><div className="stat-label">{label}</div><div className="stat-value">{total(k)}</div><div className="stat-bottom"><span>{sub}</span><span className="stat-mini">0{i+1}</span></div><div className="stat-swoosh"/></Link>)}</section>
    <section className="insights-grid"><div className="panel booking-panel"><div className="panel-heading"><div><div className="eyebrow">RESERVATION PULSE</div><h3>Bookings at a glance</h3></div><Link to="/bookings" className="text-link">View bookings <span>→</span></Link></div><div className="booking-total"><strong>{total('bookings')}</strong><span>total reservations</span></div>{bookingBreakdown.length ? <div className="status-list">{bookingBreakdown.map(([status, n], i) => <div className="status-row" key={status}><div className="status-name"><span className={`status-swatch swatch-${i%4}`}/>{nice(status)}</div><div className="status-track"><span style={{width:`${Math.max(5, n / Math.max(1, data.bookings.length) * 100)}%`}}/></div><strong>{n}</strong></div>)}</div> : <div className="empty-insight">{data.bookings ? 'Your booking activity will appear here.' : 'Booking service is not responding right now.'}</div>}<div className="panel-footnote"><span>✳</span> A little visibility goes a long way.</div></div>
      <div className="panel activity-panel"><div className="panel-heading"><div><div className="eyebrow">THE LATEST</div><h3>Recent bookings</h3></div><Link to="/bookings" className="round-link">↗</Link></div>{recent.length ? <div className="recent-list">{recent.map((r,i) => <div className="recent-item" key={r.id ?? i}><div className={`recent-symbol recent-${i%4}`}>▤</div><div className="recent-main"><strong>Booking #{r.id ?? '—'}</strong><small>{r.customerId ? `Customer #${r.customerId}` : 'Rental reservation'}{r.vehicleId ? ` · Vehicle #${r.vehicleId}` : ''}</small></div><div className="recent-end"><Badge value={r.status || 'Pending'}/><small>{r.bookingDate || r.createdAt ? new Date(r.bookingDate || r.createdAt).toLocaleDateString('en-IN',{day:'numeric',month:'short'}) : 'Recently'}</small></div></div>)}</div> : <div className="empty-insight">{data.bookings ? 'No bookings to show just yet.' : 'Recent activity will appear once the service is available.'}</div>}<div className="activity-footer"><div className="activity-avatars"><span>R</span><span>+</span></div><span>Every journey starts with a booking.</span></div></div></section>
    <section className="bottom-strip"><div className="bottom-mark">✦</div><div><strong>Small details. Smoother journeys.</strong><p>Keep your fleet, bookings and people moving in sync.</p></div><div className="bottom-links"><Link to="/trips">Manage trips <span>↗</span></Link><Link to="/payments">Review payments <span>↗</span></Link></div></section>
  </div>
}
