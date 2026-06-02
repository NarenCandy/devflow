import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '@/api/axios';
import { toast } from 'sonner';
import Navbar from '@/components/Navbar';
import IssueBoard from '@/components/IssueBoard';
import { Plus, Trash2, ArrowLeft } from 'lucide-react';

const EMPTY_ISSUE = { title: '', description: '', status: 'TODO' };
const PRIORITY_STYLES = {
  LOW:    'bg-[#EAF3DE] text-[#3B6D11]',
  MEDIUM: 'bg-[#FAEEDA] text-[#854F0B]',
  HIGH:   'bg-[#FCEBEB] text-[#A32D2D]',
};

export default function ProjectDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [issues, setIssues] = useState([]);
  const [fetching, setFetching] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(EMPTY_ISSUE);
  const [loading, setLoading] = useState(false);
  const [members, setMembers] = useState([]);
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviting, setInviting] = useState(false);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [confirmMemberId, setConfirmMemberId] = useState(null);

  useEffect(() => {
    Promise.all([
      api.get(`/project/${id}`),
      api.get(`/project/${id}/issues`),
    ]).then(([projRes, issuesRes]) => {
      setProject(projRes.data);
      setIssues(issuesRes.data);
      setMembers(projRes.data.members || []);
    }).catch(() => toast.error('Failed to load project'))
      .finally(() => setFetching(false));
  }, [id]);

  const handleInvite = async (e) => {
    e.preventDefault();
    if (!inviteEmail.trim()) return;
    setInviting(true);
    try {
      const { data: updated } = await api.post(`/project/${id}/members`, { email: inviteEmail });
      setMembers(updated.members || []);
      toast.success(`${inviteEmail} added to project!`);
      setInviteEmail('');
    } catch (err) {
      toast.error(err.response?.data?.message || 'User not found');
    } finally {
      setInviting(false);
    }
  };

  const handleRemoveMemberConfirm = async () => {
    try {
      const { data: updated } = await api.delete(`/project/${id}/members/${confirmMemberId}`);
      setMembers(updated.members || []);
      toast.success('Member removed');
    } catch {
      toast.error('Failed to remove member');
    } finally {
      setConfirmMemberId(null);
    }
  };

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleCreateIssue = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const { data: newIssue } = await api.post(`/project/${id}/issue`, form);
      setIssues([...issues, newIssue]);
      setForm(EMPTY_ISSUE);
      setShowForm(false);
      toast.success('Issue created!');
    } catch {
      toast.error('Failed to create issue');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteProjectConfirm = async () => {
    try {
      await api.delete(`/project/${id}`);
      toast.success('Project deleted');
      navigate('/');
    } catch {
      toast.error('Failed to delete project');
      setConfirmDelete(false);
    }
  };

  const handleIssueDelete = (issueId) => setIssues(issues.filter((i) => i.id !== issueId));
  const handleStatusChange = (issueId, newStatus) =>
    setIssues(issues.map((i) => (i.id === issueId ? { ...i, status: newStatus } : i)));

  if (fetching) return (
    <div className="min-h-screen bg-[#F8F8FB]">
      <Navbar />
      <div className="max-w-6xl mx-auto px-6 py-10">
        <p className="text-sm text-gray-400">Loading project...</p>
      </div>
    </div>
  );

  if (!project) return <div className="min-h-screen bg-[#F8F8FB]"><Navbar /></div>;

  const p = PRIORITY_STYLES[project.priority] || PRIORITY_STYLES.MEDIUM;

  return (
    <div className="min-h-screen bg-[#F8F8FB]">
      <Navbar />
      <main className="max-w-6xl mx-auto px-6 py-10 space-y-6">

        {/* header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <button onClick={() => navigate('/')}
              className="p-1.5 rounded-lg hover:bg-white border border-transparent hover:border-gray-100 text-gray-400 hover:text-gray-600 transition-all">
              <ArrowLeft className="w-4 h-4" />
            </button>
            <div>
              <div className="flex items-center gap-2">
                <h1 className="text-xl font-medium text-gray-900">{project.projectName}</h1>
                <span className={`text-[11px] font-medium px-2 py-0.5 rounded-full ${p}`}>
                  {project.priority}
                </span>
              </div>
              {project.description && (
                <p className="text-sm text-gray-400 mt-0.5">{project.description}</p>
              )}
            </div>
          </div>
          <div className="flex items-center gap-2">
            <button onClick={() => setShowForm(!showForm)}
              className="flex items-center gap-2 bg-[#534AB7] hover:bg-[#4740a0] text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors">
              <Plus className="w-4 h-4" /> New issue
            </button>

            {/* project delete inline confirm */}
            {confirmDelete ? (
              <div className="flex items-center gap-1.5">
                <span className="text-xs text-gray-400">Delete project?</span>
                <button onClick={handleDeleteProjectConfirm}
                  className="text-xs font-medium text-white bg-red-500 hover:bg-red-600 px-2 py-1 rounded-md transition-colors">
                  Yes
                </button>
                <button onClick={() => setConfirmDelete(false)}
                  className="text-xs text-gray-400 hover:text-gray-600 px-2 py-1 rounded-md hover:bg-gray-100 transition-colors">
                  No
                </button>
              </div>
            ) : (
              <button onClick={() => setConfirmDelete(true)}
                className="p-2 rounded-lg text-gray-300 hover:text-red-400 hover:bg-red-50 border border-transparent hover:border-red-100 transition-all">
                <Trash2 className="w-4 h-4" />
              </button>
            )}
          </div>
        </div>

        {/* create issue form */}
        {showForm && (
          <div className="bg-white border border-gray-100 rounded-xl p-5">
            <p className="text-sm font-medium text-gray-900 mb-4">New issue</p>
            <form onSubmit={handleCreateIssue} className="space-y-3">
              <input name="title" value={form.title} onChange={handleChange}
                placeholder="Issue title" required
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors" />
              <input name="description" value={form.description} onChange={handleChange}
                placeholder="Description (optional)"
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors" />
              <select name="status" value={form.status} onChange={handleChange}
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] bg-white">
                <option value="TODO">To Do</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="DONE">Done</option>
              </select>
              <div className="flex gap-2 pt-1">
                <button type="submit" disabled={loading}
                  className="bg-[#534AB7] hover:bg-[#4740a0] text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors disabled:opacity-50">
                  {loading ? 'Creating...' : 'Create issue'}
                </button>
                <button type="button" onClick={() => setShowForm(false)}
                  className="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100 transition-colors">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* issue board */}
        <IssueBoard issues={issues} onDelete={handleIssueDelete} onStatusChange={handleStatusChange} />

        {/* members panel */}
        <div className="bg-white border border-gray-100 rounded-xl p-6 space-y-4">
          <p className="text-sm font-medium text-gray-900">
            Team members <span className="text-gray-400 font-normal">({members.length})</span>
          </p>
          <div className="space-y-2">
            {members.length === 0 ? (
              <p className="text-sm text-gray-400">No members yet. Invite someone below.</p>
            ) : (
              members.map((m) => (
                <div key={m.id} className="flex items-center justify-between py-2 border-b border-gray-50 last:border-0">
                  <div className="flex items-center gap-3">
                    <div className="w-7 h-7 rounded-full bg-[#EEEDFE] flex items-center justify-center text-[11px] font-medium text-[#534AB7]">
                      {m.name?.charAt(0).toUpperCase()}
                    </div>
                    <div>
                      <p className="text-sm font-medium text-gray-700">{m.name}</p>
                      <p className="text-xs text-gray-400">{m.email}</p>
                    </div>
                  </div>

                  {/* member remove inline confirm */}
                  {confirmMemberId === m.id ? (
                    <div className="flex items-center gap-1.5">
                      <span className="text-xs text-gray-400">Remove?</span>
                      <button onClick={handleRemoveMemberConfirm}
                        className="text-xs font-medium text-white bg-red-500 hover:bg-red-600 px-2 py-1 rounded-md transition-colors">
                        Yes
                      </button>
                      <button onClick={() => setConfirmMemberId(null)}
                        className="text-xs text-gray-400 hover:text-gray-600 px-2 py-1 rounded-md hover:bg-gray-100 transition-colors">
                        No
                      </button>
                    </div>
                  ) : (
                    <button onClick={() => setConfirmMemberId(m.id)}
                      className="text-xs text-gray-300 hover:text-red-400 transition-colors">
                      Remove
                    </button>
                  )}
                </div>
              ))
            )}
          </div>
          <form onSubmit={handleInvite} className="flex gap-2 pt-1">
            <input type="email" value={inviteEmail}
              onChange={(e) => setInviteEmail(e.target.value)}
              placeholder="Invite by email..."
              className="flex-1 border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors" />
            <button type="submit" disabled={inviting}
              className="bg-[#534AB7] hover:bg-[#4740a0] text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors disabled:opacity-50">
              {inviting ? 'Adding...' : 'Invite'}
            </button>
          </form>
        </div>
      </main>
    </div>
  );
}