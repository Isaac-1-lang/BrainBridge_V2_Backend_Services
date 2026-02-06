"use client";
import React, { useState, useEffect } from 'react';
import { Bookmark, ArrowUpRight, Clock, Eye } from "lucide-react";

export default function PublicProjectCard({ project }) {
  const [isSaved, setIsSaved] = useState(false);

  // Sync state with localStorage on mount
  useEffect(() => {
    const favs = JSON.parse(localStorage.getItem("favorited_ids") || "[]");
    setIsSaved(favs.includes(project.id));
  }, [project.id]);

  const handleSave = (e) => {
    e.preventDefault(); // Stop navigation
    e.stopPropagation(); // Stop parent bubble

    const favs = JSON.parse(localStorage.getItem("favorited_ids") || "[]");
    let updatedFavs;

    if (favs.includes(project.id)) {
      updatedFavs = favs.filter(id => id !== project.id);
      setIsSaved(false);
    } else {
      updatedFavs = [...favs, project.id];
      setIsSaved(true);
    }

    localStorage.setItem("favorited_ids", JSON.stringify(updatedFavs));
  };

  return (
    <div className="group cursor-pointer">
      {/* Image with Bookmark Icon */}
      <div className="relative aspect-[16/10] overflow-hidden rounded-2xl bg-gray-100 mb-5">
        <img 
          src={project.image} 
          className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105" 
          alt={project.name}
        />
        <div className="absolute inset-0 bg-black/5 group-hover:bg-black/0 transition-colors" />
        
        <button 
          onClick={handleSave}
          className="absolute top-4 right-4 p-2.5 bg-white/90 backdrop-blur-md rounded-full shadow-sm hover:bg-white transition-all transform active:scale-90 z-20"
        >
          <Bookmark 
            size={18} 
            /* Changed from red-500 to blue-600 (#2563eb) for the saved state */
            className={`transition-colors ${isSaved ? 'fill-blue-600 text-blue-600' : 'text-gray-600'}`} 
          />
        </button>
      </div>

      {/* Header Info */}
      <div className="space-y-3">
        <div className="flex items-center justify-between text-[#6941C6] font-semibold text-xs uppercase tracking-wider">
          <span>{project.category}</span>
          <div className="flex items-center gap-1.5 text-gray-400">
            <Clock size={12} />
            <span>{project.date || "Just now"}</span>
          </div>
        </div>

        <div className="flex items-start justify-between">
          <h4 className="text-2xl font-bold text-[#101828] group-hover:text-[#6941C6] transition-colors leading-tight">
            {project.name}
          </h4>
          <ArrowUpRight className="text-gray-300 group-hover:text-[#6941C6] transition-transform group-hover:translate-x-1 group-hover:-translate-y-1" size={24} />
        </div>

        <p className="text-[#475467] text-sm leading-relaxed line-clamp-2">
          {project.description}
        </p>

        {/* Footer Info */}
        <div className="flex items-center justify-between pt-4">
          <div className="flex items-center gap-2">
            <div className="w-6 h-6 rounded-full bg-gray-100 flex items-center justify-center text-[10px] font-bold text-gray-600 border border-gray-200 uppercase">
              {project.creator ? project.creator[0] : "U"}
            </div>
            <span className="text-xs font-medium text-gray-600">{project.creator}</span>
          </div>
          
          <div className="flex items-center gap-3">
             <div className="flex items-center gap-1 text-gray-400 text-xs font-medium">
               <Eye size={14} />
               <span>{project.views || 0}</span>
             </div>
             <div className="flex gap-1">
               {project.tech && project.tech.slice(0, 1).map(t => (
                 <span key={t} className="px-2 py-0.5 bg-[#F9F5FF] text-[#6941C6] rounded text-[10px] font-bold uppercase">{t}</span>
               ))}
             </div>
          </div>
        </div>
      </div>
    </div>
  );
}