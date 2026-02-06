import React from 'react';

const categories = [
  'All', 'Web Apps', 'AI / ML', 'IoT', 'FinTech', 'HealthTech', 'Education', 'Open Source'
];

export default function CategoryFilter({ active, setActive }) {
  return (
    /* Removed -mx-2 and pb-4 which were causing the container collision */
    /* Added overflow-y-hidden to ensure only horizontal scrolling exists */
    <div className="flex gap-3 overflow-x-auto overflow-y-hidden no-scrollbar py-2">
      {categories.map((cat) => (
        <button
          key={cat}
          type="button"
          onClick={() => setActive(cat)}
          className={`px-6 py-3 rounded-2xl text-[10px] font-black uppercase tracking-widest transition-all whitespace-nowrap border flex-shrink-0 ${
            active === cat 
            ? 'bg-[#3A38DE] text-white border-[#3A38DE] shadow-lg shadow-blue-500/20' 
            : 'bg-white text-gray-400 border-gray-100 hover:border-gray-300 hover:text-[#08075C]'
          }`}
        >
          {cat}
        </button>
      ))}
    </div>
  );
}