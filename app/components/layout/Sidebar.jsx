"use client";
import React, { useState, useEffect } from 'react'; // Added useState, useEffect
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { 
  LayoutDashboard, 
  Layers, 
  Heart, 
  Compass, 
  Mail, 
  LogOut 
} from 'lucide-react';

const workspaceLinks = [
  { name: 'Dashboard', icon: <LayoutDashboard size={18} />, path: '/dashboard' },
  { name: 'My Projects', icon: <Layers size={18} />, path: '/dashboard/projects' },
  { name: 'Favorites', icon: <Heart size={18} />, path: '/dashboard/favorites' },
];

const discoveryLinks = [
  { name: 'Explore Hub', icon: <Compass size={18} />, path: '/projects' },
  { name: 'Inbox', icon: <Mail size={18} />, path: '/dashboard/inbox' },
];

export default function Sidebar() {
  const pathname = usePathname();
  
  /* --- HYDRATION FIX START --- */
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);
  /* --- HYDRATION FIX END --- */

  const NavItem = ({ item }) => {
    // Only determine active state if the component has mounted on the client
    const isActive = mounted && (pathname === item.path || (item.path !== '/dashboard' && pathname.startsWith(item.path)));
    
    return (
      <Link 
        href={item.path}
        className={`group relative flex items-center gap-4 px-4 py-3 rounded-xl font-bold transition-all duration-300 ${
          isActive 
          ? 'bg-[#3A38DE]/5 text-[#3A38DE]' 
          : 'text-[#667085] hover:text-[#08075C] hover:bg-gray-50'
        }`}
      >
        {/* Active Indicator Pillar */}
        {isActive && (
          <div className="absolute left-0 w-1 h-5 bg-[#3A38DE] rounded-r-full shadow-[0_0_8px_rgba(58,56,222,0.4)]" />
        )}

        <div className={`transition-transform duration-300 ${isActive ? 'scale-110' : 'group-hover:scale-110'}`}>
          {item.icon}
        </div>

        <span className="text-[13px] tracking-tight">{item.name}</span>
      </Link>
    );
  };

  return (
    <aside className="w-64 min-h-screen bg-white border-r border-gray-100 flex flex-col fixed left-0 top-0 z-50">
      {/* Brand Section */}
      <div className="p-8">
        <h1 className="text-2xl font-bold bg-clip-text text-transparent w-fit transition-opacity hover:opacity-80"
          style={{ 
            fontFamily: "'Playpen Sans', cursive", 
            backgroundImage: "linear-gradient(90deg, #08075C, #3A38DE)" 
          }}>
          BrainBridge
        </h1>
      </div>

      <nav className="flex-1 px-4 space-y-8">
        {/* Workspace Section */}
        <div>
          <p className="px-4 text-[10px] font-black text-gray-300 uppercase tracking-[0.2em] mb-4">Core Workspace</p>
          <div className="space-y-1">
            {workspaceLinks.map(item => <NavItem key={item.path} item={item} />)}
          </div>
        </div>

        {/* Discovery Section */}
        <div>
          <p className="px-4 text-[10px] font-black text-gray-300 uppercase tracking-[0.2em] mb-4">Discovery</p>
          <div className="space-y-1">
            {discoveryLinks.map(item => <NavItem key={item.path} item={item} />)}
          </div>
        </div>
      </nav>

      {/* Logout Footer */}
      <div className="p-6 border-t border-gray-50">
        <button className="flex items-center gap-4 px-4 py-3 w-full text-[#667085] font-bold hover:text-red-500 hover:bg-red-50 rounded-xl transition-all group">
          <LogOut size={18} className="group-hover:-translate-x-1 transition-transform" />
          <span className="text-[13px] tracking-tight">Logout</span>
        </button>
      </div>
    </aside>
  );
}