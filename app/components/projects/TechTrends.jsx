"use client";
import React from 'react';
import { TrendingUp, Cpu, Code2, Terminal, Zap } from 'lucide-react';

const trends = [
  { 
    name: 'React', 
    count: '1.2k', 
    icon: <Code2 size={16} />, 
    intensity: 'text-[#3A38DE]', // Primary Brand Blue
    bgIntensity: 'bg-[#3A38DE]/10' 
  },
  { 
    name: 'Nest.js', 
    count: '850', 
    icon: <Zap size={16} />, 
    intensity: 'text-[#08075C]', // Deep Navy Blue
    bgIntensity: 'bg-[#08075C]/10' 
  },
  { 
    name: 'Python', 
    count: '2.4k', 
    icon: <Terminal size={16} />, 
    intensity: 'text-[#5351FB]', // Lighter Electric Blue
    bgIntensity: 'bg-[#5351FB]/10' 
  },
  { 
    name: 'Rust', 
    count: '310', 
    icon: <Cpu size={16} />, 
    intensity: 'text-[#1A18A0]', // Mid-tone Royal Blue
    bgIntensity: 'bg-[#1A18A0]/10' 
  },
];

export default function TechTrends() {
  return (
    <div className="flex flex-col gap-6 mb-12">
      {/* Small subtle header */}
      <div className="flex items-center gap-2 px-1">
        <TrendingUp size={14} className="text-[#3A38DE]" />
        <p className="text-[10px] font-bold text-gray-400 uppercase tracking-[0.2em]">
        Trending Technologies
        </p>
      </div>

      {/* Grid of Nodes */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {trends.map((tech) => (
          <div 
            key={tech.name} 
            className="group flex items-center justify-between p-4 rounded-xl border border-gray-100 bg-white hover:border-[#3A38DE]/30 hover:shadow-[0_4px_20px_-10px_rgba(58,56,222,0.1)] transition-all cursor-default"
          >
            <div className="flex items-center gap-3">
              {/* Icon Container with varying Blue intensity */}
              <div className={`w-10 h-10 rounded-lg ${tech.bgIntensity} ${tech.intensity} flex items-center justify-center transition-transform group-hover:scale-110`}>
                {tech.icon}
              </div>
              
              <div>
                <p className="text-xs font-bold text-[#101828] group-hover:text-[#3A38DE] transition-colors">
                  {tech.name}
                </p>
                <div className="flex items-center gap-1.5">
                  {/* Numbers using the same Blue scale */}
                  <span className={`text-[11px] font-bold ${tech.intensity}`}>
                    {tech.count}
                  </span>
                  <span className="text-[10px] font-medium text-gray-400 uppercase tracking-tighter">
                    projects
                  </span>
                </div>
              </div>
            </div>
            
            {/* Subtle trend indicator in Blue */}
            <div className="hidden lg:flex items-center text-[#3A38DE] bg-[#3A38DE]/5 px-2 py-0.5 rounded text-[8px] font-bold uppercase tracking-tighter">
              +12%
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}