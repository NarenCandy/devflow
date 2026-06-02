import { useState, useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { toast } from 'sonner';
import api from '@/api/axios';
import Navbar from '@/components/Navbar';
import { ArrowLeft, Send } from 'lucide-react';

const STATUS_STYLES = {
  TODO:        'bg-gray-100 text-gray-600',
  IN_PROGRESS: 'bg-[#EEEDFE] text-[#534AB7]',
  DONE:        'bg-[#EAF3DE] text-[#3B6D11]',
};

export default function IssueDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const [issue, setIssue] = useState(state?.issue || null);
  const [issueLoading, setIssueLoading] = useState(!state?.issue);
  
  const [comments, setComments] = useState([]);
  const [commentText, setCommentText] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [editing, setEditing] = useState(false);
  const [searchQ, setSearchQ] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [assigning, setAssigning] = useState(false);
  const [editForm, setEditForm] = useState({
    title: issue?.title || '',
    description: issue?.description || '',
    status: issue?.status || '',
  });

  useEffect(() => {
    if (id) {
      api.get(`/issue/${id}/comments`)
        .then(res => setComments(res.data))
        .catch(() => setComments([]));
    }
  }, [id]);

  useEffect(() => {
    if (!state?.issue && id) {
      api.get(`/issue/${id}`)
        .then(res => setIssue(res.data))
        .catch(() => toast.error('Failed to load issue'))
        .finally(() => setIssueLoading(false));
    }
    }, [id]);

  const handleSearch = async (e) => {
    const q = e.target.value;
    setSearchQ(q);
    if (q.length < 2) { setSearchResults([]); return; }
    try {
      const { data } = await api.get(`/users/search?q=${q}`);
      setSearchResults(data);
    } catch {
      setSearchResults([]);
    }
  };

  const handleAssign = async (userId) => {
    setAssigning(true);
    try {
      const { data: updated } = await api.patch(`/issue/${id}/assign/${userId}`);
      setIssue(updated);
      const assignedName = searchResults.find(u => u.id === userId)?.name;
      toast.success(`Assigned to ${assignedName}!`);
      setSearchQ('');
      setSearchResults([]);
    } catch {
      toast.error('Failed to assign user');
    } finally {
      setAssigning(false);
    }
  };

  const handleEditChange = (e) => setEditForm({ ...editForm, [e.target.name]: e.target.value });

  const handleEditSave = async () => {
    try {
      const { data: updated } = await api.put(`/issue/${id}`, editForm);
      setIssue(updated);
      setEditing(false);
      toast.success('Issue updated!');
    } catch {
      toast.error('Failed to update issue');
    }
  };

  const handleAddComment = async (e) => {
    e.preventDefault();
    if (!commentText.trim()) return;
    setSubmitting(true);
    try {
      const { data: newComment } = await api.post(`/issue/${id}/comment`, { content: commentText });
      setComments([...comments, newComment]);
      setCommentText('');
      toast.success('Comment added!');
    } catch {
      toast.error('Failed to add comment');
    } finally {
      setSubmitting(false);
    }
  };
  if (issueLoading) {
    return (
      <div className="min-h-screen bg-[#F8F8FB]">
        <Navbar />
        <div className="max-w-3xl mx-auto px-6 py-10">
          <p className="text-sm text-gray-400">Loading issue...</p>
        </div>
      </div>
    );
  }

  if (!issue) {
    return (
      <div className="min-h-screen bg-[#F8F8FB]">
        <Navbar />
        <div className="max-w-3xl mx-auto px-6 py-10">
          <p className="text-sm text-gray-400">Issue not found.</p>
          <button onClick={() => navigate(-1)} className="text-sm text-[#534AB7] mt-2 hover:underline">
            Go back
          </button>
        </div>
      </div>
    );
  }

  const statusStyle = STATUS_STYLES[issue.status] || STATUS_STYLES.TODO;

  return (
    <div className="min-h-screen bg-[#F8F8FB]">
      <Navbar />
      <main className="max-w-3xl mx-auto px-6 py-10 space-y-5">
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-1.5 text-sm text-gray-400 hover:text-gray-600 transition-colors"
        >
          <ArrowLeft className="w-4 h-4" /> Back
        </button>

        {/* issue card */}
        <div className="bg-white border border-gray-100 rounded-xl p-6 space-y-4">
          <div className="flex items-start justify-between gap-3">
            <div className="flex-1">
              {editing ? (
                <input
                  name="title"
                  value={editForm.title}
                  onChange={handleEditChange}
                  className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm font-medium outline-none focus:border-[#534AB7]"
                />
              ) : (
                <h1 className="text-base font-medium text-gray-900">{issue.title}</h1>
              )}
              <p className="text-xs text-gray-400 mt-1">
                Posted by {issue.postedBy} · Project #{issue.projectId}
              </p>
            </div>
            <button
              onClick={() => editing ? handleEditSave() : setEditing(true)}
              className="text-xs font-medium text-[#534AB7] bg-[#EEEDFE] hover:bg-[#e0defa] px-3 py-1.5 rounded-md transition-colors shrink-0"
            >
              {editing ? 'Save' : 'Edit'}
            </button>
          </div>

          {editing ? (
            <div className="space-y-3">
              <input
                name="description"
                value={editForm.description}
                onChange={handleEditChange}
                placeholder="Description"
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7]"
              />
              <select
                name="status"
                value={editForm.status}
                onChange={handleEditChange}
                className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] bg-white"
              >
                <option value="TODO">To Do</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="DONE">Done</option>
              </select>

              {/* assign section */}
              <div className="pt-3 border-t border-gray-50 space-y-2">
                <p className="text-xs font-medium text-gray-500">Assign to</p>
                <div className="relative">
                  <input
                    value={searchQ}
                    onChange={handleSearch}
                    placeholder="Search team member..."
                    className="w-full border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors"
                  />
                  {searchResults.length > 0 && (
                    <div className="absolute top-full left-0 right-0 bg-white border border-gray-100 rounded-lg shadow-sm mt-1 z-10 overflow-hidden">
                      {searchResults.map((u) => (
                        <button
                          key={u.id}
                          onClick={() => handleAssign(u.id)}
                          disabled={assigning}
                          className="w-full flex items-center gap-3 px-3 py-2.5 hover:bg-gray-50 transition-colors text-left"
                        >
                          <div className="w-6 h-6 rounded-full bg-[#EEEDFE] flex items-center justify-center text-[10px] font-medium text-[#534AB7]">
                            {u.name?.charAt(0).toUpperCase()}
                          </div>
                          <div>
                            <p className="text-sm font-medium text-gray-700">{u.name}</p>
                            <p className="text-xs text-gray-400">{u.email}</p>
                          </div>
                        </button>
                      ))}
                    </div>
                  )}
                </div>
              </div>
              <button
                onClick={() => setEditing(false)}
                className="text-sm text-gray-400 hover:text-gray-600"
              >
                Cancel
              </button>
            </div>
          ) : (
            <div className="space-y-3">
              <p className="text-sm text-gray-600 leading-relaxed">
                {issue.description || 'No description.'}
              </p>
              <div className="flex gap-2">
                <span className={`text-xs font-medium px-2.5 py-1 rounded-full ${statusStyle}`}>
                  {issue.status?.replace('_', ' ')}
                </span>
                {issue.assignedTo && (
                  <span className="text-xs font-medium px-2.5 py-1 rounded-full bg-gray-100 text-gray-600">
                    Assigned: {issue.assignedTo}
                  </span>
                )}
              </div>
            </div>
          )}
        </div>

        {/* comments */}
        <div className="bg-white border border-gray-100 rounded-xl p-6 space-y-4">
          <p className="text-sm font-medium text-gray-900">
            Comments <span className="text-gray-400 font-normal">({comments.length})</span>
          </p>
          {comments.length === 0 ? (
            <p className="text-sm text-gray-400">No comments yet.</p>
          ) : (
            <div className="space-y-3">
              {comments.map((c) => (
                <div key={c.id} className="border border-gray-100 rounded-lg p-3 space-y-1">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-medium text-gray-700">
                      {c.postedBy?.name || 'Unknown'}
                    </span>
                    <span className="text-xs text-gray-400">
                      {c.createdAt ? new Date(c.createdAt).toLocaleDateString() : ''}
                    </span>
                  </div>
                  <p className="text-sm text-gray-600">{c.content}</p>
                </div>
              ))}
            </div>
          )}
          <form onSubmit={handleAddComment} className="flex gap-2 pt-1">
            <input
              value={commentText}
              onChange={(e) => setCommentText(e.target.value)}
              placeholder="Write a comment..."
              className="flex-1 border border-gray-200 rounded-lg px-3 py-2 text-sm outline-none focus:border-[#534AB7] transition-colors"
            />
            <button
              type="submit"
              disabled={submitting || !commentText.trim()}
              className="bg-[#534AB7] hover:bg-[#4740a0] text-white px-3 py-2 rounded-lg transition-colors disabled:opacity-50"
            >
              <Send className="w-4 h-4" />
            </button>
          </form>
        </div>
      </main>
    </div>
  );
}