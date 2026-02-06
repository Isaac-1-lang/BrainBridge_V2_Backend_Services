"use client";
import React from 'react';
import { 
  Search, 
  Bell, 
  ChevronDown, 
  Languages 
} from 'lucide-react';

export default function TopBar() {
  return (
    <header className="h-20 bg-white/80 backdrop-blur-md border-b border-gray-100 flex items-center justify-between px-8 sticky top-0 z-40 ml-64">
      {/* Search Input - Optimized Visibility */}
      <div className="relative w-96">
        <Search 
          className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" 
          size={16} 
        />
        <input 
          type="text" 
          placeholder="Search projects, stack, or partners..." 
          className="w-full bg-gray-100/50 border border-gray-100 rounded-xl py-2.5 pl-11 pr-4 outline-none focus:border-[#3A38DE] focus:ring-4 focus:ring-[#3A38DE]/5 transition-all text-sm text-[#08075C] font-medium placeholder:text-gray-400"
        />
      </div>

      {/* Right Side Actions */}
      <div className="flex items-center gap-6">
        {/* Language & Notifications */}
        <div className="flex items-center gap-3 border-r border-gray-100 pr-6">
          <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg hover:bg-gray-50 transition-colors">
            <Languages size={14} className="text-[#3A38DE]" />
            <span className="text-xs font-bold text-[#08075C]">EN</span>
          </button>
          
          <button className="relative p-2.5 rounded-xl hover:bg-gray-50 transition-all text-gray-400 hover:text-[#3A38DE]">
            <Bell size={20} />
            {/* Notification Dot with Blue Scale */}
            <span className="absolute top-2.5 right-2.5 w-2 h-2 bg-[#3A38DE] rounded-full border-2 border-white"></span>
          </button>
        </div>

        {/* User Profile Summary */}
        <div className="flex items-center gap-3 cursor-pointer group p-1 rounded-xl hover:bg-gray-50 transition-all">
          <div className="text-right hidden sm:block">
            <p className="text-sm font-bold text-[#08075C]">John Developer</p>
            <p className="text-[10px] font-black text-gray-400 uppercase tracking-widest">Admin Node</p>
          </div>
          
          {/* Avatar with Brand Gradient */}
          <div className="w-10 h-10 bg-gradient-to-br from-[#08075C] to-[#3A38DE] rounded-xl flex items-center justify-center text-white font-bold text-sm shadow-md shadow-blue-500/10 transition-transform group-hover:scale-105">
            JD
          </div>
          
          <ChevronDown 
            size={14} 
            className="text-gray-400 group-hover:text-[#3A38DE] transition-colors" 
          />
        </div>
      </div>
    </header>
  );
}