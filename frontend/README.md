# Vehicle Rental – React Frontend

React + Vite + React Router + Axios + Tailwind. Talks only to the API Gateway.

## Run
```bash
npm install
cp .env.example .env     # optional; default is http://localhost:8080
npm run dev              # http://localhost:5173
npm run build            # production build in dist/
```
Dev mode proxies `/api/*` to `VITE_API_BASE_URL` (CORS-free). Production builds call `VITE_API_BASE_URL` directly, so enable CORS on the gateway or serve `dist/` behind the same host.

## Structure
- `src/config/resources.js` – one config per entity (fields, validation, columns, dropdown refs)
- `src/components/ResourcePage.jsx` – generic CRUD page (table + modal form + delete confirm)
- `src/api/client.js` – axios instance + backend error parser

## UI refresh (Roam)
The interface has been refreshed with a premium muted palette: warm grey/ivory surfaces, forest-green navigation, sage accents, and restrained copper/clay highlights. The dashboard is responsive and uses live data from the existing API client. Existing CRUD pages, forms, validation, and API Gateway routes have been kept in place.

### Backend integration notes
- Development proxy forwards `/api/*` to `http://localhost:8080` by default. Set `VITE_API_BASE_URL` if your API Gateway runs elsewhere.
- The supplied backend archive currently exposes routes for customers, vehicles, drivers, bookings, payments, trips, and feedbacks through the gateway config.
- No authentication/role endpoints were present in the supplied backend files, so admin/customer login and role-based screens are not fabricated in this version.
