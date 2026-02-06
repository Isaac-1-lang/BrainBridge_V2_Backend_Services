"use client";
import React, { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import { ArrowLeft, MessageSquare, Share2, Terminal, Activity } from 'lucide-react';
import Link from 'next/link';
import ProjectView from "../../components/projects/ProjectView"; 

export default function ProjectPage() {
  const { id } = useParams();
  const router = useRouter();
  const [project, setProject] = useState(null);

  useEffect(() => {
    const saved = localStorage.getItem('my_projects');
    const userProjects = saved ? JSON.parse(saved) : [];
    
    const initialData = [
      { id: "p1", name: "Quantum Ledger", creator: "Alex Rivers", category: "Fintech", description: "Decentralized protocol for high-frequency quantum trading.", tech: ["Rust", "Solidity"], status: "Active Node", image: "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?auto=format&fit=crop&w=900&q=80" },
      { id: "p2", name: "CryptoSec AI", creator: "Sarah Chen", category: "AI / ML", description: "LLM-driven monitoring for global trading groups.", tech: ["Python", "PyTorch"], status: "Operational", image: "https://images.unsplash.com/photo-1640161704729-cbe966a08476?auto=format&fit=crop&w=800&q=80" }
    ];

    const found = [...userProjects, ...initialData].find(p => p.id.toString() === id.toString());
    if (found) setProject(found);
  }, [id]);

  const handleCollaborate = () => {
    const message = `Hi ${project.creator}, I'm interested in collaborating on the "${project.name}" node.`;
    const params = new URLSearchParams({
      newChat: "true",
      project: project.name,
      to: project.creator || "Architect",
      msg: message
    });
    router.push(`/dashboard/inbox?${params.toString()}`);
  };

  if (!project) return (
    <div className="min-h-screen flex items-center justify-center bg-[#F8FAFC]">
       <p className="text-[10px] font-black uppercase tracking-[0.3em] text-[#08075C] animate-pulse">Initialising Module...</p>
    </div>
  );

  return (
    <div className="bg-[#F8FAFC] min-h-screen pb-20">
      <div className="max-w-7xl mx-auto px-6 py-8 flex justify-between items-center">
        <Link href="/projects" className="inline-flex items-center gap-2 text-[10px] font-black uppercase tracking-widest text-gray-400 hover:text-[#3A38DE] transition-all">
          <ArrowLeft size={14} /> Discovery Hub
        </Link>
        <button className="p-2.5 bg-white border border-gray-100 rounded-xl text-gray-400 hover:text-[#08075C] transition-all"><Share2 size={16} /></button>
      </div>

      {/* FORCE HIDE BUTTONS: 
        We wrap ProjectView in a div that targets any button tags 
        inside the ProjectView and sets them to display: none.
      */}
      <div className="public-view-only-wrapper [&_button]:hidden">
         <ProjectView project={project} />
      </div>

      <div className="max-w-7xl mx-auto px-6 mt-12 grid grid-cols-1 lg:grid-cols-3 gap-8">
        <section className="lg:col-span-2 bg-white p-10 rounded-[2.5rem] border border-gray-100 shadow-sm relative overflow-hidden group">
          <div className="relative z-10">
            <h3 className="text-[10px] font-black text-[#3A38DE] uppercase tracking-[0.2em] mb-4">Request Access</h3>
            <h2 className="text-3xl font-black text-[#08075C] mb-6 tracking-tight">Collaborate with {project.creator}</h2>
            <p className="text-gray-500 text-sm leading-relaxed mb-8 max-w-xl">
              Initiate a direct secure channel to discuss technical specifications and development goals.
            </p>
            
            <button 
              onClick={handleCollaborate}
              className="flex items-center gap-4 bg-[#08075C] text-white px-8 py-4 rounded-2xl text-[10px] font-black uppercase tracking-[0.2em] hover:bg-[#3A38DE] transition-all shadow-xl shadow-blue-900/10 active:scale-95"
            >
              <MessageSquare size={18} />
              Start Collaboration Chat
            </button>
          </div>
        </section>

        <div className="space-y-8">
           <section className="bg-[#08075C] p-8 rounded-[2.5rem] text-white">
              <h3 className="text-[9px] font-black text-blue-300 uppercase tracking-[0.2em] mb-6 flex items-center gap-2">
                <Terminal size={14} /> System Profile
              </h3>
              <div className="space-y-4">
                <div className="flex justify-between items-center py-2 border-b border-white/10">
                  <span className="text-[10px] text-gray-400 font-bold uppercase tracking-widest">Architect</span>
                  <span className="text-xs font-black">{project.creator}</span>
                </div>
                <div className="flex justify-between items-center py-2 border-b border-white/10">
                  <span className="text-[10px] text-gray-400 font-bold uppercase tracking-widest">Visibility</span>
                  <span className="text-xs font-black text-blue-400 uppercase tracking-widest">Global Node</span>
                </div>
                <div className="flex justify-between items-center py-2">
                   <span className="text-[10px] text-gray-400 font-bold uppercase tracking-widest">Verified</span>
                   <Activity size={14} className="text-green-400" />
                </div>
              </div>
           </section>
        </div>
      </div>
    </div>
  );
}