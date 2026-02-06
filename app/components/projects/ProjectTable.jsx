"use client";
import React, { useState } from 'react';
import Link from 'next/link';
import { 
  Users, 
  ExternalLink, 
  Trash2, 
  AlertCircle, 
  X, 
  Check 
} from 'lucide-react';

export default function ProjectTable({ projects, onDelete }) {
  const [deletingId, setDeletingId] = useState(null);

  const handleDeleteClick = (id) => {
    // First click: Show confirmation UI for this specific row
    setDeletingId(id);
  };

  const cancelDelete = () => {
    setDeletingId(null);
  };

  const confirmDelete = (id) => {
    onDelete(id);
    setDeletingId(null);
  };

  return (
    <div className="bg-white rounded-3xl border border-gray-100 overflow-hidden shadow-sm">
      <table className="w-full text-left border-collapse">
        <thead>
          <tr className="border-b border-gray-50 bg-[#F8F9FF]/50">
            <th className="px-8 py-5 text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Project Node</th>
            <th className="px-6 py-5 text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Phase</th>
            <th className="px-6 py-5 text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Stack</th>
            <th className="px-6 py-5 text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Team</th>
            <th className="px-8 py-5 text-right text-[10px] font-black text-gray-400 uppercase tracking-[0.2em]">Actions</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-50">
          {projects.map((proj) => {
            const isConfirming = deletingId === proj.id;

            return (
              <tr 
                key={proj.id} 
                className={`transition-all duration-300 ${
                  isConfirming ? 'bg-red-50/50' : 'hover:bg-[#3A38DE]/5 group'
                }`}
              >
                <td className="px-8 py-6">
                  <p className="text-sm font-bold text-[#08075C]">{proj.name}</p>
                  <p className="text-[10px] text-gray-400 font-medium uppercase tracking-tight mt-0.5">
                    {proj.category}
                  </p>
                </td>
                
                <td className="px-6 py-6">
                  <span className={`px-2.5 py-1 rounded-md text-[9px] font-black uppercase tracking-wider border ${
                    proj.status === 'Completed' 
                    ? 'bg-blue-50 text-[#3A38DE] border-blue-100' 
                    : 'bg-white text-gray-500 border-gray-200'
                  }`}>
                    {proj.status}
                  </span>
                </td>

                <td className="px-6 py-6">
                  <div className="flex gap-1.5">
                    {proj.tech?.slice(0, 2).map(t => (
                      <span key={t} className="text-[9px] text-[#3A38DE] font-bold bg-[#3A38DE]/5 px-2 py-0.5 rounded border border-[#3A38DE]/10 uppercase">
                        {t}
                      </span>
                    ))}
                  </div>
                </td>

                <td className="px-6 py-6">
                  <div className="flex items-center gap-1.5 text-[#08075C] font-bold text-xs">
                    <Users size={12} className="text-gray-300" />
                    {proj.members?.length || 1}
                  </div>
                </td>

                <td className="px-8 py-6 text-right">
                  {!isConfirming ? (
                    <div className="flex items-center justify-end gap-4">
                      <Link 
                        href={`/dashboard/projects/${proj.id}`} 
                        className="flex items-center gap-1.5 text-[#3A38DE] hover:text-[#08075C] text-[10px] font-black uppercase transition-colors"
                      >
                        Open <ExternalLink size={12} />
                      </Link>
                      <button 
                        onClick={() => handleDeleteClick(proj.id)} 
                        className="flex items-center gap-1.5 text-gray-300 hover:text-red-500 text-[10px] font-black uppercase transition-all"
                      >
                        Archive <Trash2 size={12} />
                      </button>
                    </div>
                  ) : (
                    <div className="flex items-center justify-end gap-2 animate-in fade-in slide-in-from-right-2 duration-300">
                      <span className="text-[9px] font-black text-red-500 uppercase mr-2 flex items-center gap-1">
                        <AlertCircle size={10} /> Confirm?
                      </span>
                      <button 
                        onClick={() => confirmDelete(proj.id)}
                        className="p-1.5 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors shadow-sm shadow-red-200"
                      >
                        <Check size={14} strokeWidth={3} />
                      </button>
                      <button 
                        onClick={cancelDelete}
                        className="p-1.5 bg-white border border-gray-200 text-gray-400 rounded-lg hover:bg-gray-50 transition-colors"
                      >
                        <X size={14} strokeWidth={3} />
                      </button>
                    </div>
                  )}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}