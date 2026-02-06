"use client";
import React from 'react';
import Link from 'next/link'; // Added Link
import { 
  MoreVertical, 
  ArrowUpRight, 
  Users, 
  Image as ImageIcon 
} from "lucide-react";

export default function ProjectCard({ project }) {
  // Construct the dynamic path based on project ID
  const viewPath = `/dashboard/projects/${project.id}`;

  return (
    <div className="bg-white rounded-2xl border border-gray-100 shadow-sm hover:shadow-[0_12px_30px_-10px_rgba(8,7,92,0.15)] hover:border-[#3A38DE]/20 transition-all duration-300 group flex flex-col overflow-hidden h-full">
      
      {/* 1. Visual Header */}
      <div className="relative h-40 w-full bg-[#F8F9FB] overflow-hidden">
        {project.image ? (
          <img 
            src={project.image} 
            alt={project.name} 
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700 ease-out"
          />
        ) : (
          <div className="w-full h-full flex flex-col items-center justify-center text-[#3A38DE]/20">
            <ImageIcon size={32} strokeWidth={1.5} />
            <span className="text-[10px] font-bold mt-2 uppercase tracking-widest">No Preview</span>
          </div>
        )}

        <div className="absolute top-4 left-4">
          <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-white/90 backdrop-blur-md border border-white shadow-sm text-[9px] font-black uppercase tracking-wider text-[#08075C]">
            <span className={`w-1.5 h-1.5 rounded-full animate-pulse ${
              project.status === 'Completed' ? 'bg-blue-400' : 'bg-[#3A38DE]'
            }`}></span>
            {project.status}
          </span>
        </div>
      </div>

      {/* 2. Content Section */}
      <div className="p-5 flex flex-col flex-1">
        <div className="flex justify-between items-start mb-2">
          {/* We make the title a link too, very common UX pattern */}
          <Link href={viewPath}>
            <h4 className="text-[15px] font-bold text-[#08075C] leading-tight truncate group-hover:text-[#3A38DE] transition-colors cursor-pointer">
              {project.name}
            </h4>
          </Link>
          <button className="text-gray-300 hover:text-[#08075C] transition-colors p-1 -mr-1">
            <MoreVertical size={16} />
          </button>
        </div>
        
        <p className="text-[#475467] text-[12px] leading-relaxed line-clamp-2 mb-4 h-9">
          {project.description}
        </p>

        <div className="flex flex-wrap gap-1.5 mb-6">
          {project.tech.map((tag) => (
            <span key={tag} className="text-[9px] font-black text-[#3A38DE] bg-[#3A38DE]/5 px-2 py-0.5 rounded border border-[#3A38DE]/10 uppercase tracking-tighter">
              {tag}
            </span>
          ))}
        </div>

        {/* 3. Footer Section */}
        <div className="mt-auto pt-4 border-t border-gray-50 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <div className="flex -space-x-2">
              <div className="w-6 h-6 rounded-lg bg-[#08075C] flex items-center justify-center text-white border-2 border-white shadow-sm">
                <Users size={10} />
              </div>
              <div className="w-6 h-6 rounded-lg bg-[#3A38DE] flex items-center justify-center text-[8px] text-white border-2 border-white font-black">
                +{project.members?.length || 0}
              </div>
            </div>
            <span className="text-[9px] text-gray-400 font-black uppercase tracking-widest">Collaborators</span>
          </div>
          
          {/* THE FIX: Changed from button to Link */}
          <Link 
            href={viewPath}
            className="group/btn flex items-center gap-1.5 text-[10px] font-black tracking-widest text-[#3A38DE] bg-[#3A38DE]/5 hover:bg-[#3A38DE] hover:text-white px-3 py-2 rounded-xl transition-all uppercase"
          >
            MANAGE 
            <ArrowUpRight size={14} className="group-hover/btn:translate-x-0.5 group-hover/btn:-translate-y-0.5 transition-transform" />
          </Link>
        </div>
      </div>
    </div>
  );
}