"use client";
import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { 
  ArrowLeft, 
  PenSquare, 
  Github, 
  Trash2, 
  Quote, 
  Code2, 
  Crown, 
  Calendar, 
  Binary,
  ExternalLink 
} from 'lucide-react';
import EditProjectModal from './EditProjectModal';

export default function ProjectView({ project, onUpdate, onDelete }) {
  const router = useRouter();
  const [isEditOpen, setIsEditOpen] = useState(false);

  if (!project) return null;

  const getStatusTheme = (status) => {
    const map = {
      'Idea': { bg: 'bg-amber-50', text: 'text-amber-600', dot: 'bg-amber-500', border: 'border-amber-100' },
      'In Progress': { bg: 'bg-blue-50', text: 'text-blue-700', dot: 'bg-blue-500', border: 'border-blue-100' },
      'Completed': { bg: 'bg-green-50', text: 'text-green-700', dot: 'bg-green-500', border: 'border-green-100' },
      'Stopped': { bg: 'bg-red-50', text: 'text-red-700', dot: 'bg-red-500', border: 'border-red-100' }
    };
    return map[status] || { bg: 'bg-gray-50', text: 'text-gray-600', dot: 'bg-gray-400', border: 'border-gray-100' };
  };

  const theme = getStatusTheme(project.status);

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="relative h-[400px] w-full overflow-hidden bg-blue-900/70">
        <button 
          onClick={() => router.push('/dashboard')}
          className="absolute top-8 left-8 z-30 text-white/70 hover:text-white flex items-center gap-2 text-xs font-bold uppercase tracking-widest transition-all group"
        >
          <ArrowLeft size={14} className="group-hover:-translate-x-1 transition-transform" /> 
          Back to System
        </button>

        {project.image ? (
          <div className="absolute inset-0">
            <img 
              src={project.image} 
              className="w-full h-full object-cover opacity-20 grayscale" 
              alt={project.name}
            />
            <div className="absolute inset-0 bg-gradient-to-t from-blue-900/70 via-blue-900/50 to-transparent" />
          </div>
        ) : (
          <div className="absolute inset-0 bg-blue-900/50" />
        )}

        <div className="absolute inset-0 flex items-end">
          <div className="max-w-7xl mx-auto w-full px-8 pb-12">
            <div className="flex flex-col md:flex-row md:items-end justify-between gap-8">
              <div className="space-y-4">
                <div className="flex items-center gap-2">
                  <span className="bg-blue-600/80 text-white text-xs font-bold uppercase px-3 py-1 rounded-md shadow-sm">
                    {project.category}
                  </span>
                  <span className="text-white/50 text-xs font-bold uppercase">
                    Node ID: {project.id?.toString().padStart(4, '0') || '7721'}
                  </span>
                </div>
                <h1 className="text-4xl md:text-6xl font-extrabold text-white tracking-tight leading-none">
                  {project.name}
                </h1>
              </div>

              <div className="flex items-center gap-4">
                <button 
                  onClick={() => setIsEditOpen(true)}
                  className="bg-white text-blue-900 px-6 py-3 rounded-lg text-xs font-bold uppercase tracking-wider hover:bg-blue-700 hover:text-white transition-all flex items-center gap-2 shadow-sm"
                >
                  <PenSquare size={14} />
                  Modify
                </button>
                {project.github && (
                  <a 
                    href={project.github} 
                    target="_blank" 
                    className="bg-white/10 backdrop-blur-sm text-white border border-white/20 px-6 py-3 rounded-lg text-xs font-bold uppercase tracking-wider flex items-center gap-2 hover:bg-white/20 transition-all"
                  >
                    <Github size={16} />
                    Source Code
                  </a>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-7xl mx-auto px-6 -mt-10 relative z-20 pb-16">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">

          {/* Main Column */}
          <div className="lg:col-span-8 space-y-6">
            {/* Project Description */}
            <section className="bg-white p-8 rounded-2xl shadow-sm border border-gray-100 relative">
              <Quote className="absolute -top-4 -right-4 text-blue-900/10 w-24 h-24" />
              <h3 className="text-xs font-bold text-gray-400 uppercase tracking-wider mb-6 flex items-center gap-2">
                <div className="w-8 h-[1px] bg-gray-200"></div>
                Project Core Intelligence
              </h3>
              <p className="text-xl text-gray-900 font-semibold leading-relaxed italic border-l-4 border-blue-600 pl-6">
                {project.description}
              </p>
            </section>

            {/* Technical Details */}
            <section className="bg-white p-6 rounded-2xl border border-gray-100">
              <div className="flex items-center gap-2 mb-4">
                <Code2 size={16} className="text-blue-700" />
                <h4 className="font-bold text-gray-900 uppercase tracking-wide text-sm">Architectural Overview</h4>
              </div>
              <p className="text-gray-600 text-sm leading-7">
                The <span className="font-bold text-gray-900">"{project.name}"</span> infrastructure is currently in <span className={`px-2 py-0.5 rounded text-xs font-bold uppercase tracking-wide ${theme.bg} ${theme.text}`}>{project.status}</span> stage. 
                Stack: <span className="text-blue-700 font-semibold">{project.tech?.[0] || 'Custom Frameworks'}</span>.
              </p>
            </section>

            {/* Danger Zone */}
            <section className="bg-red-50 p-6 rounded-2xl border border-red-100 flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center text-red-600">
                    <Trash2 size={18} />
                </div>
                <div>
                    <h4 className="text-red-800 font-bold text-sm uppercase mb-1">Archive Entity</h4>
                    <p className="text-red-600/70 text-xs italic">This will remove all node connections.</p>
                </div>
              </div>
              <button 
                onClick={onDelete}
                className="bg-white border border-red-200 hover:bg-red-600 hover:text-white text-red-600 px-4 py-2 rounded-lg text-xs font-bold uppercase tracking-wide transition-all shadow-sm"
              >
                Delete
              </button>
            </section>
          </div>

          {/* Sidebar */}
          <div className="lg:col-span-4 space-y-4">
            {/* Node Status */}
            <div className={`${theme.bg} ${theme.border} p-6 rounded-2xl border flex items-center justify-between shadow-sm`}>
              <div>
                <p className={`text-[9px] font-bold ${theme.text} uppercase tracking-wide mb-1`}>Node Status</p>
                <p className={`text-lg font-bold ${theme.text} uppercase tracking-tight`}>{project.status}</p>
              </div>
              <div className="relative">
                <div className={`w-3 h-3 rounded-full ${theme.dot} animate-ping absolute inset-0 opacity-20`} />
                <div className={`w-3 h-3 rounded-full ${theme.dot}`} />
              </div>
            </div>

            {/* Tech Stack */}
            <div className="bg-gray-100/70 p-6 rounded-2xl border border-gray-200">
              <h4 className="text-gray-700 text-xs font-bold uppercase tracking-wide mb-4 flex items-center gap-2">
                  <Binary size={12} /> Tech Stack
              </h4>
              <div className="flex flex-wrap gap-2">
                {project.tech?.map(skill => (
                  <span key={skill} className="bg-white/40 text-gray-900 border border-gray-200 px-3 py-1.5 rounded-lg text-xs font-semibold cursor-default">
                    {skill}
                  </span>
                ))}
              </div>
            </div>

            {/* Team */}
            <div className="bg-white p-6 rounded-2xl border border-gray-100 shadow-sm">
              <h4 className="text-gray-900 text-xs font-bold uppercase tracking-wide mb-4">Personnel</h4>
              <div className="space-y-4">
                {project.members?.map(member => (
                  <div key={member} className="flex items-center gap-4">
                    <div className="w-12 h-12 rounded-lg bg-gray-50 border border-gray-200 flex items-center justify-center text-gray-900 text-xs font-bold uppercase shadow-inner">
                      {member[0]}
                    </div>
                    <div>
                        <p className="text-xs font-bold text-gray-900">@{member.toLowerCase()}</p>
                        <p className="text-[9px] text-gray-500 font-medium uppercase tracking-wide">Contributor</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>

            {/* Metadata */}
            <div className="grid grid-cols-2 gap-2">
              <div className="bg-white p-4 rounded-2xl border border-gray-100 flex flex-col items-center justify-center text-center">
                <Calendar size={14} className="text-gray-400 mb-1" />
                <p className="text-[8px] font-bold text-gray-400 uppercase mb-1">Deployment</p>
                <p className="text-[10px] font-bold text-gray-900">
                  {project.createdAt ? new Date(project.createdAt).toLocaleDateString() : 'FEB 2026'}
                </p>
              </div>
              <div className="bg-white p-4 rounded-2xl border border-gray-100 flex flex-col items-center justify-center text-center">
                <ExternalLink size={14} className="text-gray-400 mb-1" />
                <p className="text-[8px] font-bold text-gray-400 uppercase mb-1">Environment</p>
                <p className="text-[10px] font-bold text-blue-700 uppercase tracking-tight">
                    {project.projectType || 'Staging'}
                </p>
              </div>
            </div>
          </div>

        </div>
      </div>

      <EditProjectModal 
        isOpen={isEditOpen} 
        onClose={() => setIsEditOpen(false)} 
        onUpdateProject={onUpdate} 
        project={project}
      />
    </div>
  );
}
