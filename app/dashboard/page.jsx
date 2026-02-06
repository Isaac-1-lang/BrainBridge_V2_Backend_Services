"use client";
import React, { useState, useMemo, useEffect } from "react";
import StatsOverview from "../components/dashboard/StatsOverview";
import ProjectCard from "../components/dashboard/ProjectCard";
import NewProjectModal from "../components/projects/NewProjectModal";

const DEFAULT_PROJECT_IMAGES = [
  "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?q=80&w=800&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?q=80&w=800&auto=format&fit=crop",
  "https://images.unsplash.com/photo-1639322537228-f710d846310a?q=80&w=800&auto=format&fit=crop",
];

export default function DashboardPage() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");
  const [projects, setProjects] = useState([]);

  const categories = ["All", "Web App", "Mobile", "AI/ML", "Design", "Blockchain"];

  useEffect(() => {
    const savedProjects = localStorage.getItem("my_projects");
    if (savedProjects) {
      setProjects(JSON.parse(savedProjects));
    } else {
      const defaultProjects = [
        {
          id: "1",
          name: "BrainBridge UI",
          description: "Design system and component library for the core platform.",
          status: "Completed",
          category: "Design",
          tech: ["Next.js", "Tailwind"],
          image: DEFAULT_PROJECT_IMAGES[0]
        }
      ];
      setProjects(defaultProjects);
      localStorage.setItem("my_projects", JSON.stringify(defaultProjects));
    }
  }, []);

  const filteredProjects = useMemo(() => {
    return projects.filter((proj) => {
      const matchesSearch = proj.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (proj.tech && proj.tech.some((t) => t.toLowerCase().includes(searchQuery.toLowerCase())));
      const matchesCategory = activeCategory === "All" || proj.category === activeCategory;
      return matchesSearch && matchesCategory;
    });
  }, [searchQuery, activeCategory, projects]);

  const addProject = (newProj) => {
    const id = Date.now().toString();
    
    // Check if user uploaded an image. If not, use default.
    const projectWithId = { 
      ...newProj, 
      id,
      image: newProj.image || DEFAULT_PROJECT_IMAGES[projects.length % DEFAULT_PROJECT_IMAGES.length]
    };

    const updatedFleet = [projectWithId, ...projects];
    setProjects(updatedFleet);
    localStorage.setItem("my_projects", JSON.stringify(updatedFleet));
    
    setIsModalOpen(false); 
    // Redirect removed: localhost:3000/projects/... will no longer happen
  };

  return (
    <div className="max-w-[1200px] mx-auto px-4 sm:px-6 pb-20">
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 mb-8">
        <div>
          <h2 className="text-2xl font-bold text-[#08075C]">Welcome, John</h2>
          <p className="text-gray-400 text-xs font-medium uppercase tracking-widest mt-0.5">Project Overview • Feb 2026</p>
        </div>

        <button
          onClick={() => setIsModalOpen(true)}
          className="bg-[#08075C] hover:bg-[#3A38DE] text-white px-4 py-2.5 rounded-xl font-black text-[10px] tracking-widest transition-all shadow-lg shadow-blue-900/10 flex items-center gap-2 w-fit uppercase"
        >
          <i className="fa-solid fa-plus text-[10px]" />
          New Project
        </button>
      </div>

      <StatsOverview projects={projects} />

      <div className="mt-12 flex flex-col md:flex-row gap-4 items-center justify-between bg-white p-2 rounded-2xl border border-gray-100 shadow-sm">
        <div className="relative w-full md:w-80">
          <i className="fa-solid fa-magnifying-glass absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 text-xs" />
          <input
            type="text"
            placeholder="Search Tech or Names..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full bg-gray-50 border-none rounded-xl py-2.5 pl-10 pr-4 text-xs font-bold text-[#08075C] outline-none focus:ring-1 focus:ring-[#3A38DE]"
          />
        </div>

        <div className="flex gap-2 overflow-x-auto no-scrollbar w-full md:w-auto">
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => setActiveCategory(cat)}
              className={`px-4 py-2 rounded-xl text-[9px] font-black uppercase tracking-widest transition-all whitespace-nowrap ${
                activeCategory === cat
                  ? "bg-[#3A38DE] text-white shadow-md"
                  : "bg-white text-gray-400 hover:text-[#08075C]"
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </div>

      <section className="mt-8">
        <h3 className="text-xs font-black uppercase tracking-[0.2em] text-[#08075C] border-l-2 border-[#3A38DE] pl-3 mb-5">
          {activeCategory} Projects ({filteredProjects.length})
        </h3>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredProjects.map((proj) => (
            <ProjectCard key={proj.id} project={proj} />
          ))}
        </div>
      </section>

      <NewProjectModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onAddProject={addProject}
      />
    </div>
  );
}