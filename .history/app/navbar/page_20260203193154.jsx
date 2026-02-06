"use client";

import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  FolderCode, 
  Heart, 
  Mail, 
  LogOut,
  Search, 
  Bell, 
  ChevronDown 
} from 'lucide-react';

export default function NavigationShell({ children }) {
  const [activeTab, setActiveTab] = useState('dashboard');

  return (
    <div className="flex min-h-screen bg-[#F5F6FA] font-sans">
      
      {/* SIDEBAR */}
      <aside className="w-64 bg-white flex flex-col fixed h-full z-30 border-r border-slate-200">
        <div className="p-7">
          <h1 className="text-2xl font-black text-[#08075C] tracking-tight">
            BrainBridge
          </h1>
        </div>

        <nav className="flex-1 px-4 mt-2 space-y-1">
          <NavItem 
            icon={<LayoutDashboard size={20} />} 
            label="Dashboard" 
            active={activeTab === 'dashboard'} 
            onClick={() => setActiveTab('dashboard')} 
          />
          <NavItem 
            icon={<FolderCode size={20} />} 
            label="Projects" 
            active={activeTab === 'projects'} 
            onClick={() => setActiveTab('projects')} 
          />
          <NavItem 
            icon={<Heart size={20} />} 
            label="Favorites" 
            active={activeTab === 'favorites'} 
            onClick={() => setActiveTab('favorites')} 
          />
          <NavItem 
            icon={<Mail size={20} />} 
            label="Inbox" 
            badge="6"
            active={activeTab === 'inbox'} 
            onClick={() => setActiveTab('inbox')} 
          />
        </nav>

        {/* LOGOUT AT BOTTOM */}
        <div className="p-4 border-t border-slate-100">
          <NavItem 
            icon={<LogOut size={20} />} 
            label="Logout" 
            color="text-red-500 hover:bg-red-50 hover:text-red-600" 
          />
        </div>
      </aside>

      {/* RIGHT SIDE CONTENT AREA */}
      <div className="flex-1 ml-64 flex flex-col">
        
        {/* TOP NAVIGATION BAR */}
        <header className="h-16 bg-white border-b border-slate-200 flex items-center justify-between px-8 sticky top-0 z-20">
          <div className="relative w-96">
            <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={18} />
            <input 
              type="text" 
              placeholder="Search" 
              className="w-full bg-[#F5F6FA] border-none rounded-full py-2 pl-12 pr-4 text-sm font-medium focus:ring-2 focus:ring-[#3A38DE]/20 transition-all outline-none" 
            />
          </div>
          
          <div className="flex items-center gap-6">
            <button className="relative p-2 hover:bg-slate-50 rounded-full transition-colors">
              <Bell className="text-slate-600" size={22} />
              <span className="absolute top-1.5 right-1.5 bg-red-500 text-white text-[9px] font-black w-4 h-4 flex items-center justify-center rounded-full border-2 border-white">
                6
              </span>
            </button>

            <div className="flex items-center gap-3 cursor-pointer border-l pl-6 border-slate-200 group">
              <div className="text-right hidden md:block">
                <p className="text-sm font-black text-[#08075C] leading-none">Moni Roy</p>
                <p className="text-[10px] font-bold text-slate-400 mt-1">Admin</p>
              </div>
              <div className="w-10 h-10 rounded-full overflow-hidden border-2 border-[#3A38DE]/10 group-hover:border-[#3A38DE]/40 transition-all">
                <img src="https://i.pravatar.cc/150?u=moni" alt="User Profile" />
              </div>
              <ChevronDown size={14} className="text-slate-400" />
            </div>
          </div>
        </header>

        {/* MAIN PAGE CONTENT */}
        <main className="p-8">
          {children}
        </main>
      </div>
    </div>
  );
}

/* SUB-COMPONENT: NavItem */
function NavItem({ icon, label, active, onClick, badge, color = "text-slate-500 hover:bg-[#F5F6FA] hover:text-[#3A38DE]" }) {
  return (
    <button 
      onClick={onClick}
      className={`
        w-full flex items-center justify-between px-4 py-3 rounded-xl cursor-pointer transition-all duration-200 font-bold
        ${active 
          ? 'bg-[#3A38DE] text-white shadow-lg shadow-[#3A38DE]/25' 
          : `${color}`}
      `}
    >
      <div className="flex items-center gap-4">
        {icon}
        <span className="text-sm tracking-tight">{label}</span>
      </div>
      {badge && (
        <span className={`
          text-[10px] px-2 py-0.5 rounded-full font-black
          ${active ? 'bg-white text-[#3A38DE]' : 'bg-[#3A38DE] text-white'}
        `}>
          {badge}
        </span>
      )}
    </button>
  );
}