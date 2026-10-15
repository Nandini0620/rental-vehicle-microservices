import { useState } from 'react'
import { NavLink, Outlet, useLocation } from 'react-router-dom'

export const NAV = [
  ['/', 'Overview', '◫', 'Workspace'],
  ['/customers', 'Customers', '♙', 'Management'],
  ['/vehicles', 'Fleet', '◇', 'Management'],
  ['/drivers', 'Drivers', '♧', 'Management'],
  ['/bookings', 'Bookings', '▤', 'Operations'],
  ['/trips', 'Trips', '↗', 'Operations'],
  ['/payments', 'Payments', '＄', 'Finance'],
  ['/feedback', 'Reviews', '☆', 'Insights'],
]
const TITLES = Object.fromEntries(NAV.map(([to, label]) => [to, label]))

export default function Layout() {
  const [open, setOpen] = useState(false)
  const location = useLocation()
  const title = TITLES[location.pathname] || 'Overview'
  return (
    <div className="app-shell">
      {open && <button className="mobile-scrim" aria-label="Close menu" onClick={() => setOpen(false)} />}
      <aside className={`sidebar ${open ? 'sidebar-open' : ''}`}>
        <div className="brand-lockup">
          <div className="brand-mark"><span>r</span><i>✦</i></div>
          <div><div className="brand-name">roam<span>.</span></div><div className="brand-caption">MOBILITY, REIMAGINED</div></div>
          <button className="sidebar-close" onClick={() => setOpen(false)} aria-label="Close menu">×</button>
        </div>
        <div className="workspace-pill"><span className="workspace-dot"/><div><strong>Roam operations</strong><small>Management studio</small></div><span className="chevron">⌄</span></div>
        <nav className="side-nav">
          {['Workspace', 'Management', 'Operations', 'Finance', 'Insights'].map((group) => {
            const links = NAV.filter((item) => item[3] === group)
            return <div className="nav-group" key={group}><div className="nav-label">{group}</div>{links.map(([to, label, icon]) => <NavLink key={to} to={to} end={to === '/'} onClick={() => setOpen(false)} className={({ isActive }) => `nav-item ${isActive ? 'nav-active' : ''}`}><span className="nav-icon">{icon}</span><span>{label}</span>{label === 'Bookings' && <span className="nav-count">•</span>}</NavLink>)}</div>
          })}
        </nav>
        <div className="sidebar-bottom"><div className="help-card"><div className="help-icon">✳</div><strong>Need a hand?</strong><p>Your operations, a little simpler. Find tips and quick guides.</p><button onClick={() => window.open('https://spring.io/projects/spring-cloud-gateway', '_blank', 'noopener,noreferrer')}>Explore resources <span>↗</span></button></div><div className="profile-row"><div className="avatar">A</div><div className="profile-meta"><strong>Administrator</strong><small>Workspace owner</small></div><span className="profile-more">•••</span></div></div>
      </aside>
      <div className="main-column">
        <header className="topbar"><div className="topbar-left"><button className="mobile-menu" onClick={() => setOpen(true)} aria-label="Open menu">☰</button><div className="breadcrumbs">Workspace <span>/</span> <strong>{title}</strong></div></div><div className="topbar-right"><div className="gateway-status"><span className="status-pulse"/> Gateway connected</div><button className="icon-button" title="Notifications" aria-label="Notifications">♧<i/></button><div className="topbar-divider"/><div className="top-avatar">A</div></div></header>
        <main className="page-content"><Outlet /></main>
        <footer className="app-footer"><span>© 2026 Roam Mobility</span><span>Thoughtful journeys start here <b>✦</b></span></footer>
      </div>
    </div>
  )
}
