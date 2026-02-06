"use client";
import React, { useState, useEffect } from 'react';
import { useParams, useRouter } from 'next/navigation';
import ProjectView from "../../../components/projects/ProjectView"; 

export default function ProjectPage() {
  const { id } = useParams();
  const router = useRouter();
  const [project, setProject] = useState(null);

  // 1. Fetch Logic from LocalStorage
  useEffect(() => {
    const allProjects = JSON.parse(localStorage.getItem('my_projects') || '[]');
    const found = allProjects.find(p => p.id.toString() === id.toString());
    
    if (found) {
      setProject(found);
    } else {
      // Fallback for demo purposes
      const fallbacks = [{ id: "1", name: "BrainBridge UI", status: "In Progress", category: "Design", tech: ["React"] }];
      setProject(fallbacks.find(p => p.id === id));
    }
  }, [id]);

  // 2. Update Logic
  const handleUpdate = (updatedProj) => {
    const allProjects = JSON.parse(localStorage.getItem('my_projects') || '[]');
    const newProjects = allProjects.map(p => p.id.toString() === id.toString() ? updatedProj : p);
    localStorage.setItem('my_projects', JSON.stringify(newProjects));
    setProject(updatedProj);
  };

  // 3. Delete Logic
  const handleDelete = () => {
    if (window.confirm("DECOMMISSION PROJECT FROM BRAINBRIDGE?")) {
      const allProjects = JSON.parse(localStorage.getItem('my_projects') || '[]');
      const filtered = allProjects.filter(p => p.id.toString() !== id.toString());
      localStorage.setItem('my_projects', JSON.stringify(filtered));
      router.push('/dashboard/projects');
    }
  };

  if (!project) return (
    <div className="min-h-screen flex items-center justify-center bg-[#F8FAFC]">
       <p className="text-[10px] font-black uppercase tracking-widest text-[#08075C] animate-pulse">Initialising Module...</p>
    </div>
  );

  return (
    <div className="bg-[#F8FAFC] min-h-screen pb-20">
      {/* A. CORE VIEW (Hero, Description, Tech Stack) */}
      <ProjectView 
        project={project} 
        onUpdate={handleUpdate} 
        onDelete={handleDelete} 
      />

      {/* B. WORKSPACE EXTENSIONS (Collaboration & Enterprise) */}
      <div className="max-w-7xl mx-auto px-6 mt-12 grid grid-cols-1 lg:grid-cols-2 gap-8">
        
        {/* Enterprise Interest Section */}
        <section className="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm">
          <h3 className="text-[10px] font-black text-[#08075C] uppercase tracking-[0.2em] mb-6 flex items-center gap-3">
            <span className="w-2 h-2 bg-blue-500 rounded-full"></span>
            Enterprise Requests
          </h3>
          <div className="space-y-4">
            <div className="p-5 bg-gray-50 rounded-3xl border border-gray-100">
              <div className="flex justify-between items-start mb-3">
                <p className="text-xs font-black text-[#08075C]">Corporate Node A</p>
                <span className="bg-blue-100 text-blue-600 px-2 py-0.5 rounded text-[8px] font-bold uppercase">New</span>
              </div>
              <p className="text-[11px] text-gray-500 italic mb-4">"Requesting integration documentation for {project.name}."</p>
              <div className="flex gap-2">
                <button className="flex-1 bg-[#08075C] text-white py-2.5 rounded-xl text-[9px] font-black uppercase tracking-widest hover:bg-[#3A38DE] transition-all">Accept</button>
                <button className="flex-1 bg-white text-gray-400 py-2.5 rounded-xl text-[9px] font-black uppercase tracking-widest border border-gray-100 hover:text-red-500 transition-all">Decline</button>
              </div>
            </div>
          </div>
        </section>

        {/* System Log / Comments Section */}
        <section className="bg-white p-8 rounded-[2.5rem] border border-gray-100 shadow-sm flex flex-col">
          <h3 className="text-[10px] font-black text-[#08075C] uppercase tracking-[0.2em] mb-6 flex items-center gap-3">
            <span className="w-2 h-2 bg-[#3A38DE] rounded-full"></span>
            Collaboration Log
          </h3>
          <div className="flex-1 space-y-6 mb-6 max-h-[250px] overflow-y-auto pr-2 custom-scrollbar">
            <div className="flex gap-4">
              <div className="w-8 h-8 rounded-xl bg-blue-50 flex-shrink-0 flex items-center justify-center text-[10px] font-black text-[#3A38DE] border border-blue-100">
                L
              </div>
              <div>
                <p className="text-[10px] font-black text-[#08075C]">Lead Architect <span className="font-normal text-gray-400 ml-2">Logged: System Boot</span></p>
                <p className="text-xs text-gray-500 mt-1">Initial parameters set for {project.category} module.</p>
              </div>
            </div>
          </div>
          
          <div className="relative mt-auto">
            <input 
              type="text" 
              placeholder="Add log entry..." 
              className="w-full bg-gray-50 border border-gray-100 rounded-2xl px-5 py-3 text-xs outline-none focus:ring-2 focus:ring-[#3A38DE]/10 transition-all" 
            />
            <button className="absolute right-3 top-1/2 -translate-y-1/2 text-[#3A38DE] hover:scale-110 transition-transform">
              <i className="fa-solid fa-paper-plane text-sm"></i>
            </button>
          </div>
        </section>

      </div>
    </div>
  );
}