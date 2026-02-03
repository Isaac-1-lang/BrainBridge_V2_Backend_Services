"use client";

import React, { useState } from 'react';
import { 
  FolderCode, 
  Eye, 
  Users, 
  TrendingUp, 
  MoreHorizontal, 
  ExternalLink, 
  Pencil, 
  Archive,
  ArrowUpRight
} from 'lucide-react';

export default function DashboardView() {
  const [view, setView] = useState('cards'); // 'cards' or 'table'

  const projects = [
    {
      id: 1,
      name: 'NeuralBridge Protocol',
      description: 'A decentralized API for cross-chain neural network training and validation.',
      status: 'In Progress',
      stack: ['React', 'Node.js', 'Python'],
      updated: '2 hours ago'
    },
    {
      id: 2,
      name: 'Quantum Ledger Hub',
      description: 'Real-time visualization tool for enterprise-grade quantum computing assets.',
      status: 'Idea',
      stack: ['Solidity', 'Go', 'Next.js'],
      updated: '1 day ago'
    },
    {
      id: 3,
      name: 'BridgeUI Kit',
      description: 'Design system and component library for BrainBridge products.',
      status: 'Completed',
      stack: ['TypeScript', 'Tailwind', 'React'],
      updated: '3 days ago'
    },
    {
      id: 4,
      name: 'CollabStream',
      description: 'Live collaboration layer for multi-user model training sessions.',
      status: 'Stopped',
      stack: ['Elixir', 'Phoenix', 'WebRTC'],
      updated: '2 weeks ago'
    }
  ];

  return (
    <div className="space-y-10">
      {/* HEADER SECTION */}
      <div className="mb-2">
        <h2 className="text-lg font-bold text-slate-500">Welcome James 👋</h2>
        <h1 className="text-3xl font-black text-[#08075C] mt-1 tracking-tight">Dashboard</h1>
      </div>

      {/* COMPACT STAT CARDS - Height adjusted to match reference image */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard title="Projects" value="7,265" trend="+11.01%" />
        <StatCard title="Viewers" value="7,265" trend="+11.01%" />
        <StatCard title="Active Collaborators" value="7,265" trend="+11.01%" />
        <StatCard title="Interprises" value="7,265" trend="+11.01%" />
      </div>

      {/* MY PROJECTS SECTION */}
      <section className="pt-4">
        <div className="flex justify-between items-center mb-6">
            <h3 className="text-2xl font-black text-[#08075C] tracking-tight">My Projects</h3>
            <div className="flex items-center gap-4">
              <div className="inline-flex border rounded-lg overflow-hidden">
                <button onClick={() => setView('cards')} className={`px-4 py-2 text-sm ${view === 'cards' ? 'bg-[#3A38DE] text-white' : 'bg-white text-slate-600'}`}>Cards</button>
                <button onClick={() => setView('table')} className={`px-4 py-2 text-sm ${view === 'table' ? 'bg-[#3A38DE] text-white' : 'bg-white text-slate-600'}`}>Table</button>
              </div>
              <button className="bg-[#3A38DE] text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-lg shadow-[#3A38DE]/20 hover:bg-[#08075C] transition-all">
                + Create Project
              </button>
            </div>
          </div>

          {/* PROJECT LIST - Cards or Table */}
          {view === 'cards' ? (
            <div className="grid grid-cols-1 xl:grid-cols-2 gap-6">
              {projects.map(p => (
                <ProjectCard key={p.id} {...p} />
              ))}
            </div>
          ) : (
            <ProjectTable projects={projects} />
          )}
      </section>
    </div>
  );
}

/* --- UI SUB-COMPONENTS --- */

function StatCard({ title, value, trend }) {
  return (
    <div className="bg-white p-6 rounded-[1.25rem] border border-slate-100 shadow-sm flex flex-col justify-between h-[140px]">
      <div className="flex justify-between items-start">
        <p className="text-[11px] font-black text-slate-400 uppercase tracking-[0.1em]">{title}</p>
        <div className="text-[#3A38DE] opacity-20">
          {title === "Projects" && <FolderCode size={20} />}
          {title === "Viewers" && <Eye size={20} />}
          {title === "Active Collaborators" && <Users size={20} />}
          {title === "Interprises" && <TrendingUp size={20} />}
        </div>
      </div>
      <div className="flex items-end justify-between">
        <h4 className="text-2xl font-black text-[#08075C] leading-none">{value}</h4>
        <div className="flex items-center gap-1 text-[10px] font-bold text-green-600">
          <ArrowUpRight size={12} />
          <span>{trend}</span>
        </div>
      </div>
    </div>
  );
}

function ProjectCard({ name, description, status, stack, updated }) {
  const statusStyles = {
    "Idea": "bg-amber-50 text-amber-600 border-amber-100",
    "In Progress": "bg-blue-50 text-[#3A38DE] border-blue-100",
    "Completed": "bg-emerald-50 text-emerald-600 border-emerald-100",
    "Stopped": "bg-rose-50 text-rose-600 border-rose-100"
  };

  return (
    <div className="bg-white rounded-[1.75rem] p-7 border border-slate-100 hover:border-[#3A38DE]/20 transition-all group relative overflow-hidden">
      {/* Active Indicator on Hover */}
      <div className="absolute left-0 top-0 bottom-0 w-1 bg-[#3A38DE] opacity-0 group-hover:opacity-100 transition-opacity" />

      <div className="flex justify-between items-start mb-4">
        <div>
          <span className={`text-[9px] font-black uppercase px-2 py-0.5 rounded border ${statusStyles[status]}`}>
            {status}
          </span>
          <h4 className="text-xl font-black text-[#08075C] mt-2 group-hover:text-[#3A38DE] transition-colors">
            {name}
          </h4>
        </div>
        <div className="flex gap-1">
          <button className="p-2 text-slate-300 hover:text-[#3A38DE] hover:bg-slate-50 rounded-lg transition-all"><ExternalLink size={16}/></button>
          <button className="p-2 text-slate-300 hover:text-[#3A38DE] hover:bg-slate-50 rounded-lg transition-all"><Pencil size={16}/></button>
          <button className="p-2 text-slate-300 hover:text-red-500 hover:bg-red-50 rounded-lg transition-all"><Archive size={16}/></button>
        </div>
      </div>

      <p className="text-slate-500 text-sm font-medium leading-relaxed mb-6 line-clamp-1">
        {description}
      </p>

      <div className="flex flex-wrap gap-2 mb-6">
        {stack.map(tech => (
          <span key={tech} className="text-[10px] font-bold bg-[#F8F9FF] text-[#3A38DE] px-2.5 py-1 rounded-md border border-blue-50">
            {tech}
          </span>
        ))}
      </div>

      <div className="flex justify-between items-center pt-4 border-t border-slate-50">
        <span className="text-[10px] font-bold text-slate-400 uppercase tracking-widest">
          Last Sync: <span className="text-slate-600">{updated}</span>
        </span>
        <button className="text-slate-300 hover:text-[#08075C]">
          <MoreHorizontal size={20} />
        </button>
      </div>
    </div>
  );
}

function ProjectTable({ projects }) {
  const statusStyles = {
    "Idea": "bg-amber-50 text-amber-600 border-amber-100",
    "In Progress": "bg-blue-50 text-[#3A38DE] border-blue-100",
    "Completed": "bg-emerald-50 text-emerald-600 border-emerald-100",
    "Stopped": "bg-rose-50 text-rose-600 border-rose-100"
  };

  return (
    <div className="bg-white border border-slate-100 rounded-2xl overflow-x-auto">
      <table className="min-w-full divide-y divide-slate-100">
        <thead className="bg-white">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-black text-slate-400 uppercase tracking-wider">Project</th>
            <th className="px-6 py-3 text-left text-xs font-black text-slate-400 uppercase tracking-wider">Description</th>
            <th className="px-6 py-3 text-left text-xs font-black text-slate-400 uppercase tracking-wider">Status</th>
            <th className="px-6 py-3 text-left text-xs font-black text-slate-400 uppercase tracking-wider">Tech Stack</th>
            <th className="px-6 py-3 text-left text-xs font-black text-slate-400 uppercase tracking-wider">Last Updated</th>
            <th className="px-6 py-3 text-right text-xs font-black text-slate-400 uppercase tracking-wider">Actions</th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-slate-50">
          {projects.map(p => (
            <tr key={p.id} className="hover:bg-slate-50">
              <td className="px-6 py-4 whitespace-nowrap align-top">
                <div className="text-sm font-black text-[#08075C]">{p.name}</div>
              </td>
              <td className="px-6 py-4 align-top">
                <div className="text-sm text-slate-500">{p.description}</div>
              </td>
              <td className="px-6 py-4 align-top">
                <span className={`text-[11px] font-black uppercase px-2 py-1 rounded border ${statusStyles[p.status]}`}>{p.status}</span>
              </td>
              <td className="px-6 py-4 align-top">
                <div className="flex flex-wrap gap-2">
                  {p.stack.map(s => (
                    <span key={s} className="text-[11px] font-bold bg-[#F8F9FF] text-[#3A38DE] px-2.5 py-1 rounded-md border border-blue-50">{s}</span>
                  ))}
                </div>
              </td>
              <td className="px-6 py-4 align-top">
                <div className="text-sm text-slate-500">{p.updated}</div>
              </td>
              <td className="px-6 py-4 align-top text-right">
                <div className="inline-flex items-center gap-2">
                  <button title="View" className="p-2 text-slate-400 hover:text-[#3A38DE] rounded-lg"><ExternalLink size={16} /></button>
                  <button title="Edit" className="p-2 text-slate-400 hover:text-[#3A38DE] rounded-lg"><Pencil size={16} /></button>
                  <button title="Archive" className="p-2 text-slate-400 hover:text-red-500 rounded-lg"><Archive size={16} /></button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}