import Modal from './Modal.jsx'
import { Btn } from './ui.jsx'

export default function ConfirmDialog({ title = 'Confirm delete', message, busy, onConfirm, onCancel }) {
  return (
    <Modal title={title} onClose={onCancel} width="max-w-sm">
      <p className="mb-5 text-sm text-slate-600">{message}</p>
      <div className="flex justify-end gap-2">
        <Btn variant="ghost" onClick={onCancel} disabled={busy}>Cancel</Btn>
        <Btn variant="danger" onClick={onConfirm} disabled={busy}>{busy ? 'Deleting…' : 'Delete'}</Btn>
      </div>
    </Modal>
  )
}
