"use client";
import React from 'react';
import { 
  Rocket, 
  BarChart3, 
  Handshake, 
  ShieldCheck, 
  TrendingUp 
} from "lucide-react";

const stats = [
  { label: 'Projects', value: '12', growth: '+2', icon: <Rocket size={14} />, color: '#3A38DE' },
  { label: 'Viewers', value: '1.2k', growth: '+15%', icon: <BarChart3 size={14} />, color: '#08075C' },
  { label: 'Partners', value: '08', growth: '+1', icon: <Handshake size={14} />, color: '#3A38DE' },
  { label: 'Enterprise', value: '04', growth: '+12%', icon: <ShieldCheck size={14} />, color: '#08075C' },
];

export default function StatsOverview() {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
      {stats.map((stat, idx) => (
        <div 
          key={idx} 
          className="bg-white p-4 rounded-xl border border-gray-100 shadow-sm hover:border-[#3A38DE]/20 transition-all group cursor-default"
        >
          <div className="flex items-center justify-between mb-3">
            {/* Sharper, monochromatic icon container */}
            <div 
              className="w-8 h-8 rounded-lg flex items-center justify-center transition-transform group-hover:scale-110"
              style={{ backgroundColor: `${stat.color}10`, color: stat.color }} // 10% opacity hex
            >
              {stat.icon}
            </div>
            
            {/* Blue-scale compact growth indicator */}
            <div className="flex items-center gap-1 text-[9px] font-black text-[#3A38DE] bg-[#3A38DE]/5 px-2 py-0.5 rounded-full uppercase tracking-tighter">
              <TrendingUp size={10} strokeWidth={3} />
              {stat.growth}
            </div>
          </div>

          <div>
            <p className="text-[10px] font-bold text-gray-400 uppercase tracking-[0.2em] mb-0.5 transition-colors group-hover:text-gray-500">
              {stat.label}
            </p>
            <div className="flex items-baseline gap-2">
              <h3 className="text-xl font-black text-[#08075C] tracking-tight">
                {stat.value}
              </h3>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}