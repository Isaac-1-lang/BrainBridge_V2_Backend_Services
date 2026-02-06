import React from 'react';

export default function ProjectFilters({ 
  searchQuery, 
  setSearchQuery, 
  activeStatus, 
  setActiveStatus,
  sortBy,
  setSortBy 
}) {
  const statuses = ['All', 'Idea', 'In Progress', 'Completed', 'Stopped'];

  return (
    <div className="flex flex-col gap-6 mb-8">
      <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
        
        {/* A. Search Input */}
        <div className="relative w-full md:w-96 group">
          <i className="fa-solid fa-magnifying-glass absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 text-xs group-focus-within:text-[#3A38DE] transition-colors"></i>
          <input 
            type="text"
            placeholder="Search Project Name or Tech..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-white border border-gray-100 rounded-2xl py-3.5 pl-12 pr-4 text-xs font-bold text-[#08075C] shadow-sm outline-none focus:ring-2 focus:ring-[#3A38DE]/10 transition-all"
          />
        </div>

        {/* B. Sort Dropdown */}
        <div className="flex items-center gap-3 self-end md:self-auto">
          <span className="text-[10px] font-black uppercase text-gray-400 tracking-widest">Sort By:</span>
          <select 
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            className="bg-white border border-gray-100 rounded-xl px-4 py-2 text-[10px] font-bold text-[#08075C] outline-none cursor-pointer hover:border-[#3A38DE]/20 transition-all"
          >
            <option value="newest">Last Updated</option>
            <option value="alpha">Name (A-Z)</option>
          </select>
        </div>
      </div>

      {/* C. Status Pills */}
      <div className="flex gap-2 overflow-x-auto no-scrollbar pb-2">
        {statuses.map((status) => (
          <button
            key={status}
            onClick={() => setActiveStatus(status)}
            className={`px-5 py-2 rounded-xl text-[9px] font-black uppercase tracking-widest transition-all whitespace-nowrap border ${
              activeStatus === status 
              ? 'bg-[#08075C] text-white border-[#08075C] shadow-lg shadow-blue-900/10' 
              : 'bg-white text-gray-400 border-gray-100 hover:text-[#08075C] hover:border-gray-200'
            }`}
          >
            {status}
          </button>
        ))}
      </div>
    </div>
  );
}