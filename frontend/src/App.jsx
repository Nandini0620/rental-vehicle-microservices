import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout.jsx'
import ResourcePage from './components/ResourcePage.jsx'
import { RESOURCES } from './config/resources.js'
import Dashboard from './pages/Dashboard.jsx'

const routes = [['customers', 'customers'], ['vehicles', 'vehicles'], ['drivers', 'drivers'], ['bookings', 'bookings'], ['payments', 'payments'], ['trips', 'trips'], ['feedback', 'feedbacks']]

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Dashboard />} />
        {routes.map(([path, key]) => <Route key={path} path={path} element={<ResourcePage cfg={RESOURCES[key]} />} />)}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  )
}
