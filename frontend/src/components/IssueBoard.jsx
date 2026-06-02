import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import api from '@/api/axios';
import { Trash2 } from 'lucide-react';

const COLUMNS = [
  { key: 'TODO',        label: 'To Do',      style: 'bg-gray-100 text-gray-600' },
  { key: 'IN_PROGRESS', label: 'In Progress', style: 'bg-[#EEEDFE] text-[#534AB7]' },
  { key: 'DONE',        label: 'Done',        style: 'bg-[#EAF3DE] text-[#3B6D11]' },
];

export default function IssueBoard({ issues, onDelete, onStatusChange }) {
  const navigate = useNavigate();
  const [confirmId, setConfirmId] = useState(null);

  const handleDeleteConfirm = async (issueId) => {
    try {
      await api.delete(`/issue/${issueId}`);
      onDelete(issueId);
      toast.success('Issue deleted');
    } catch {
      toast.error('Failed to delete issue');
    } finally {
      setConfirmId(null);
    }
  };

  const handleStatusChange = async (e, issueId) => {
    e.stopPropagation();
    const newStatus = e.target.value;
    await api.patch(`/issue/${issueId}/status`, { status: newStatus });
    onStatusChange(issueId, newStatus);
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
      {COLUMNS.map((col) => {
        const colIssues = issues.filter((i) => i.status === col.key);
        return (
          <div key={col.key} className="space-y-2">
            <div className="flex items-center gap-2 px-1">
              <span className={`text-xs font-medium px-2.5 py-1 rounded-full ${col.style}`}>
                {col.label}
              </span>
              <span className="text-xs text-gray-400">{colIssues.length}</span>
            </div>
            <div className="space-y-2">
              {colIssues.length === 0 ? (
                <div className="border border-dashed border-gray-200 rounded-xl p-4 text-center">
                  <p className="text-xs text-gray-300">No issues</p>
                </div>
              ) : (
                colIssues.map((issue) => (
                  <div key={issue.id}
                    onClick={() => navigate(`/issues/${issue.id}`, { state: { issue } })}
                    className="bg-white border border-gray-100 rounded-xl p-3.5 cursor-pointer hover:border-gray-200 hover:shadow-sm transition-all space-y-2.5"
                  >
                    <p className="text-sm font-medium text-gray-900 leading-snug">{issue.title}</p>
                    {issue.description && (
                      <p className="text-xs text-gray-400 line-clamp-2 leading-relaxed">{issue.description}</p>
                    )}
                    <div className="flex items-center justify-between gap-2">
                      {issue.assignedTo && (
                        <span className="text-xs text-gray-400 truncate">{issue.assignedTo}</span>
                      )}
                      <div className="flex items-center gap-1.5 ml-auto"
                        onClick={(e) => e.stopPropagation()}>
                        <select value={issue.status}
                          onChange={(e) => handleStatusChange(e, issue.id)}
                          className="text-xs border border-gray-200 rounded-md px-1.5 py-1 bg-white outline-none focus:border-[#534AB7] cursor-pointer">
                          <option value="TODO">To Do</option>
                          <option value="IN_PROGRESS">In Progress</option>
                          <option value="DONE">Done</option>
                        </select>

                        {/* inline confirm */}
                        {confirmId === issue.id ? (
                          <div className="flex items-center gap-1">
                            <span className="text-xs text-gray-400">Delete?</span>
                            <button
                              onClick={(e) => { e.stopPropagation(); handleDeleteConfirm(issue.id); }}
                              className="text-xs text-white bg-red-500 hover:bg-red-600 px-2 py-0.5 rounded transition-colors">
                              Yes
                            </button>
                            <button
                              onClick={(e) => { e.stopPropagation(); setConfirmId(null); }}
                              className="text-xs text-gray-400 hover:text-gray-600 px-2 py-0.5 rounded hover:bg-gray-100 transition-colors">
                              No
                            </button>
                          </div>
                        ) : (
                          <button
                            onClick={(e) => { e.stopPropagation(); setConfirmId(issue.id); }}
                            className="p-1 rounded-md text-gray-300 hover:text-red-400 hover:bg-red-50 transition-colors">
                            <Trash2 className="w-3.5 h-3.5" />
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                ))
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}