"use client";
import React, { useState, useEffect, useMemo, useRef } from 'react';
import Link from 'next/link';
import PublicHero from '../components/projects/PublicHero';
import CategoryFilter from '../components/projects/CategoryFilter';
import PublicProjectCard from '../components/projects/PublicProjectCard';
import TechTrends from '../components/projects/TechTrends';
import NewProjectModal from "../components/projects/NewProjectModal";
import { Search, Loader2, Plus } from "lucide-react";

export default function PublicDiscoveryPage() {
  const [projects, setProjects] = useState([]);
  const [activeCategory, setActiveCategory] = useState('All');
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const observerTarget = useRef(null);

  // Load Initial Data + LocalStorage Projects
  useEffect(() => {
    const initialData = [
      { id: "p1", name: "Quantum Ledger", description: "Decentralized protocol for high-frequency quantum trading.", creator: "Alex Rivers", category: "FinTech", date: "2 hours ago", tech: ["Rust", "Solidity"], views: "2.4k", likes: "156", image: "https://images.unsplash.com/photo-1639762681485-074b7f938ba0?auto=format&fit=crop&w=900&q=80" },
      { id: "p2", name: "CryptoSec AI", description: "LLM-driven monitoring for global trading groups.", creator: "Sarah Chen", category: "AI / ML", date: "5 hours ago", tech: ["Python", "PyTorch"], views: "1.8k", likes: "312", image: "https://images.unsplash.com/photo-1640161704729-cbe966a08476?auto=format&fit=crop&w=800&q=80" },
      { id: "p3", name: "EcoMesh IoT", description: "Smart city sensor network for carbon tracking.", creator: "Marcus Thorne", category: "IoT", date: "Yesterday", tech: ["C++", "MQTT"], views: "942", likes: "88", image: "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80" },
    ];

    const saved = localStorage.getItem("my_projects");
    const userProjects = saved ? JSON.parse(saved).map(p => ({
      ...p,
      category: p.category || 'Web Apps', 
      creator: "You", // This helps the Inbox identify the user as the architect
      date: "Just now",
      views: "0",
      likes: "0"
    })) : [];

    setProjects([...userProjects, ...initialData]);
  }, []);

  // Logic to add project and persist it for both Dashboard and Public Feed
  const addProject = (newProj) => {
    const id = Date.now().toString();
    const projectWithClassification = { 
      ...newProj, 
      id, 
      creator: "You", 
      date: "Just now", 
      views: "0", 
      likes: "0",
      category: newProj.category || 'Web Apps' 
    };

    setProjects(prev => [projectWithClassification, ...prev]);

    // Save to LocalStorage so it exists when navigating to the dynamic Project Page
    const saved = localStorage.getItem("my_projects");
    const currentList = saved ? JSON.parse(saved) : [];
    localStorage.setItem("my_projects", JSON.stringify([projectWithClassification, ...currentList]));
    
    setIsModalOpen(false);
  };

  const filteredProjects = useMemo(() => {
    return projects.filter(p => {
      const matchesSearch = p.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                            p.creator.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesCat = activeCategory === 'All' || p.category === activeCategory;
      return matchesSearch && matchesCat;
    });
  }, [searchQuery, activeCategory, projects]);

  return (
    <div className="bg-white min-h-screen text-[#101828]">
      {/* Editorial Style Hero using the first/latest project */}
      <PublicHero project={projects[0]} />

      <div className="max-w-7xl mx-auto px-6 py-16">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-8 mb-12">
          <div>
            <h2 className="text-sm font-semibold text-[#6941C6] mb-2 uppercase tracking-wider">Discovery Feed</h2>
            <h3 className="text-4xl font-bold tracking-tight">Recent Project Nodes</h3>
          </div>
          
          <div className="flex items-center gap-3">
            <div className="relative group w-full md:w-80">
              <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
              <input 
                type="text" 
                placeholder="Search resources..."
                value={searchQuery}
                className="w-full bg-white border border-gray-200 rounded-lg py-3 pl-12 pr-4 text-sm focus:border-[#7F56D9] focus:ring-4 focus:ring-[#7F56D9]/5 transition-all outline-none"
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <button 
              onClick={() => setIsModalOpen(true)} 
              className="bg-[#7F56D9] text-white p-3 rounded-lg hover:bg-[#6941C6] transition-all active:scale-95 shadow-lg shadow-[#7F56D9]/20"
            >
              <Plus size={20} />
            </button>
          </div>
        </div>

        {/* Filter Bar */}
        <div className="flex items-center gap-4 border-b border-gray-100 pb-6 mb-10 overflow-x-auto no-scrollbar">
          <CategoryFilter active={activeCategory} setActive={setActiveCategory} />
        </div>

        {/* Dynamic Tech Trends Section */}
        <TechTrends />

        {/* Project Grid with Link Navigation */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-16 mt-16">
          {filteredProjects.map(proj => (
            <Link 
              href={`/projects/${proj.id}`} 
              key={proj.id} 
              className="block group transition-all duration-300 hover:-translate-y-2"
            >
              <PublicProjectCard project={proj} />
            </Link>
          ))}
        </div>

        {/* Empty State */}
        {filteredProjects.length === 0 && (
          <div className="py-20 text-center">
            <p className="text-gray-400 font-medium">No nodes found in this sector.</p>
          </div>
        )}
      </div>

      <NewProjectModal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        onAddProject={addProject} 
      />
    </div>
  );
}