import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import api from '@/api/axios';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Trash2, User } from 'lucide-react';

const STATUS_OPTIONS = ['TODO', 'IN_PROGRESS', 'DONE'];

export default function IssueCard({ issue, onDelete, onStatusChange }) {
  // WHY props instead of fetching here:
  // IssueCard doesn't own the data — ProjectDetail does.
  // Cards just display and trigger actions. The parent decides what to do with those actions.
  // This is "lifting state up" — keep data at the highest component that needs it.

  const navigate = useNavigate();

  const handleStatusChange = async (e) => {
    // stop the click from bubbling up to the card's onClick (which navigates)
    e.stopPropagation();
    const newStatus = e.target.value;
    try {
      await api.patch(`/issue/${issue.id}/status`, { status: newStatus });
      // tell the parent to update its issues array — card doesn't store issues
      onStatusChange(issue.id, newStatus);
    } catch {
      toast.error('Failed to update status');
    }
  };

  const handleDelete = async (e) => {
    e.stopPropagation(); // same reason — prevent card click navigation
    try {
      await api.delete(`/issue/${issue.id}`);
      onDelete(issue.id);
    } catch {
      toast.error('Failed to delete issue');
    }
  };

  return (
    // clicking the card navigates to issue detail
    // we pass the issue object via router state so IssueDetail doesn't need to re-fetch
    <Card
      className="cursor-pointer hover:shadow-md transition-shadow"
      onClick={() => navigate(`/issues/${issue.id}`, { state: { issue } })}
    >
      <CardContent className="p-3 space-y-2">
        <p className="font-medium text-sm leading-tight">{issue.title}</p>

        {issue.description && (
          <p className="text-xs text-muted-foreground line-clamp-2">{issue.description}</p>
        )}

        {/* assignedTo is a name string from IssueDTO */}
        {issue.assignedTo && (
          <div className="flex items-center gap-1 text-xs text-muted-foreground">
            <User className="w-3 h-3" />
            {issue.assignedTo}
          </div>
        )}

        <div className="flex items-center justify-between pt-1">
          {/* status dropdown — e.stopPropagation prevents card click when changing status */}
          <select
            value={issue.status}
            onChange={handleStatusChange}
            onClick={(e) => e.stopPropagation()}
            className="text-xs border rounded px-1 py-0.5 bg-background"
          >
            {STATUS_OPTIONS.map((s) => (
              <option key={s} value={s}>{s}</option>
            ))}
          </select>

          <Button
            variant="ghost"
            size="sm"
            className="h-6 w-6 p-0 text-red-400 hover:text-red-600"
            onClick={handleDelete}
          >
            <Trash2 className="w-3 h-3" />
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}
