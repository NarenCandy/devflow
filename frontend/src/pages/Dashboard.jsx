import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '@/api/axios';
import { toast } from 'sonner';
import Navbar from '@/components/Navbar';
import { Plus, Trash2, ArrowRight, FolderOpen } from 'lucide-react';

const PRIORITY_STYLES = {
  LOW:    { badge: 'bg-[#EAF3DE] text-[#3B6D11]', label: 'Low' },
  MEDIUM: { badge: 'bg-[#FAEEDA] text-[#854F0B]', label: 'Medium' },
  HIGH:   { badge: 'bg-[#FCEBEB] text-[#A32D2D]', label: 'High' },
};
const EMPTY_FORM = { project_name: '', description: '', status: 'ACTIVE', priority: 'MEDIUM' };

export default function Dashboard() {
  const [projects, setProjects] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState(EMPTY_FORM);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [fetching, setFetching] = useState(true);
  const [confirmId, setConfirmId] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    api.get('/projects')
      .then((res) => setProjects(res.data))
      .catch(() => toast.error('Failed to load projects'))
      .finally(() => setFetching(false));
  }, []);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleCreate = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const { data: newProject } = await api.post('/project', form);
      setProjects([...projects, newProject]);
      setForm(EMPTY_FORM);
      setShowForm(false);
      toast.success('Project created!');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create project');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    try {
      await api.delete(`/project/${confirmId}`);
      setProjects(projects.filter(p => p.id !== confirmId));
      toast.success('Project deleted');
    } catch {
      toast.error('Failed to delete project');
    } finally {
      setConfirmId(null);
    }
  };

  const totalIssues = projects.reduce((acc, p) => acc + (p.issueIds?.length || 0), 0);

  return (
    <div className="min-h-screen bg-[#F8F8FB]">
      <Navbar />
      <main className="max-w-5xl mx-auto px-6 py-10">
        {/* header */}
        <div className="flex items-start justify-between mb-8">
          <div>
            <h1 className="text-xl font-medium text-gray-900">My projects</h1>
            <p className="text-sm text-gray-400 mt-0.5">{projects.length} projects</p>
          </div>
          <button
            onClick={() => setShowForm(!showForm)}
            className="flex items-center gap-2 bg-[#534AB7] hover:bg-[#4740a0] text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors"
          >
            <Plus className="w-4 h-4" />
            New project
          </button>
        </div>

        {/* stat cards */}
        <div className="grid grid-cols-3 gap-3 mb-8">
          {[
            { label: 'Total projects', value: projects.length },
            { label: 'Total issues', value: totalIssues },
            { label: 'Active', value: projects.filter(p => p.status?.toUpperCase() === 'ACTIVE').length },
          ].map((s) => (
            <div key={s.label} className="bg-white border border-gray-100 rounded-xl p-4">
              <p className="text-xs text-gray-400 mb-1">{s.label}</p>
              <p className="text-2xl font-medium text-gray-900">{s.value}</p>
            </div>
          ))}
        </div>

        {/* create form */}
        {showForm && (
          <div className="bg-white border border-gray-100 rounded-xl p-5 mb-6">
            <p className="text-sm font-medium text-gray-900 mb-4">New project</p>
            <form onSubmit={handleCreate} className="space-y-3">
              {error && <p className="text-sm text-red-500">{error}</p>}
              <input
                name="project_name"
                value={form.project_name}
                onChange={handleChange}
                placeholder="Project name"
                required
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors"
              />
              <input
                name="description"
                value={form.description}
                onChange={handleChange}
                placeholder="Description (optional)"
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors"
              />
              <div className="grid grid-cols-2 gap-3">
                <select name="status" value={form.status} onChange={handleChange}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] bg-white">
                  <option value="ACTIVE">Active</option>
                  <option value="ON_HOLD">On Hold</option>
                  <option value="COMPLETED">Completed</option>
                </select>
                <select name="priority" value={form.priority} onChange={handleChange}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] bg-white">
                  <option value="LOW">Low</option>
                  <option value="MEDIUM">Medium</option>
                  <option value="HIGH">High</option>
                </select>
              </div>
              <div className="flex gap-2 pt-1">
                <button type="submit" disabled={loading}
                  className="bg-[#534AB7] hover:bg-[#4740a0] text-white text-sm font-medium px-4 py-2 rounded-lg transition-colors disabled:opacity-50">
                  {loading ? 'Creating...' : 'Create project'}
                </button>
                <button type="button" onClick={() => setShowForm(false)}
                  className="text-sm text-gray-500 px-4 py-2 rounded-lg hover:bg-gray-100 transition-colors">
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {/* project grid */}
        {fetching ? (
          <div className="text-center py-16">
            <p className="text-sm text-gray-400">Loading projects...</p>
          </div>
        ) : projects.length === 0 ? (
          <div className="text-center py-16 border border-dashed border-gray-200 rounded-xl">
            <FolderOpen className="w-8 h-8 text-gray-300 mx-auto mb-3" />
            <p className="text-sm text-gray-400">No projects yet. Create one to get started.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
            {projects.map((project) => {
              const p = PRIORITY_STYLES[project.priority] || PRIORITY_STYLES.MEDIUM;
              return (
                <div key={project.id} className="bg-white border border-gray-100 rounded-xl p-4 flex flex-col gap-3 hover:border-gray-200 transition-colors">
                  <div className="flex items-start justify-between gap-2">
                    <p className="text-sm font-medium text-gray-900 leading-snug">{project.projectName}</p>
                    <span className={`text-[11px] font-medium px-2 py-0.5 rounded-full shrink-0 ${p.badge}`}>
                      {p.label}
                    </span>
                  </div>
                  {project.description && (
                    <p className="text-xs text-gray-400 line-clamp-2 leading-relaxed">{project.description}</p>
                  )}
                  <div className="flex items-center justify-between pt-2 border-t border-gray-50">
                    <span className="text-xs text-gray-400">
                      {project.issueIds?.length || 0} issues
                    </span>
                    <div className="flex items-center gap-1">
                      {/* inline confirm */}
                      {confirmId === project.id ? (
                        <div className="flex items-center gap-1.5">
                          <span className="text-xs text-gray-400">Delete?</span>
                          <button onClick={handleDeleteConfirm}
                            className="text-xs font-medium text-white bg-red-500 hover:bg-red-600 px-2 py-1 rounded-md transition-colors">
                            Yes
                          </button>
                          <button onClick={() => setConfirmId(null)}
                            className="text-xs text-gray-400 hover:text-gray-600 px-2 py-1 rounded-md hover:bg-gray-100 transition-colors">
                            No
                          </button>
                        </div>
                      ) : (
                        <button onClick={() => setConfirmId(project.id)}
                          className="p-1.5 rounded-md text-gray-300 hover:text-red-400 hover:bg-red-50 transition-colors">
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      )}
                      <button onClick={() => navigate(`/projects/${project.id}`)}
                        className="flex items-center gap-1 text-xs font-medium text-[#534AB7] bg-[#EEEDFE] hover:bg-[#e0defa] px-3 py-1.5 rounded-md transition-colors">
                        Open <ArrowRight className="w-3 h-3" />
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </main>
    </div>
  );
}