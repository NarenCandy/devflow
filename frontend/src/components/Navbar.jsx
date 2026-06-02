import { useNavigate, Link } from 'react-router-dom';
import useAuth from '@/hooks/useAuth';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const initials = user?.name
    ? user.name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2)
    : '?';

  return (
    <nav className="border-b px-6 py-3 flex items-center justify-between bg-white">
      <Link to="/" className="flex items-center gap-2 font-medium text-sm tracking-tight">
        <span className="w-2 h-2 rounded-full bg-[#534AB7] inline-block" />
        DevFlow
      </Link>
      <div className="flex items-center gap-3">
        <span className="text-sm text-muted-foreground hidden sm:block">{user?.name}</span>
        <div className="w-7 h-7 rounded-full bg-[#EEEDFE] flex items-center justify-center text-[11px] font-medium text-[#534AB7]">
          {initials}
        </div>
        <button
          onClick={handleLogout}
          className="text-sm text-muted-foreground border border-border rounded-md px-3 py-1.5 hover:bg-muted flex items-center gap-1.5"
        >
          Logout
        </button>
      </div>
    </nav>
  );
}