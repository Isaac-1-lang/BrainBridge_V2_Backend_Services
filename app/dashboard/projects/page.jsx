"use client";
import React, { useState, useEffect, useMemo } from 'react';
import ProjectFilters from '../../components/projects/ProjectFilters';
import ProjectTable from '../../components/projects/ProjectTable';

export default function ProjectsWorkspace() {
  const [projects, setProjects] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeStatus, setActiveStatus] = useState('All');
  const [sortBy, setSortBy] = useState('newest');

  // Load projects from localStorage on mount
  useEffect(() => {
    const saved = JSON.parse(localStorage.getItem('my_projects') || '[]');
    setProjects(saved);
  }, []);

  // Function to handle delete
  const handleDelete = (id) => {
    const updated = projects.filter(p => p.id !== id);
    setProjects(updated);
    localStorage.setItem('my_projects', JSON.stringify(updated)); // keep localStorage in sync
  };

  const filteredAndSorted = useMemo(() => {
    let result = projects.filter(p => {
      const matchesSearch = p.name.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesStatus = activeStatus === 'All' || p.status === activeStatus;
      return matchesSearch && matchesStatus;
    });

    if (sortBy === 'alpha') {
      result.sort((a, b) => a.name.localeCompare(b.name));
    } else {
      // Logic for "Last Updated" (or by ID)
      result.sort((a, b) => b.id - a.id);
    }

    return result;
  }, [projects, searchQuery, activeStatus, sortBy]);

  return (
    <div className="max-w-7xl mx-auto px-6 py-10">
      <header className="mb-10 flex justify-between items-end">
        <div>
          <h1 className="text-2xl font-black text-[#08075C] tracking-tight uppercase">Projects</h1>
          <p className="text-gray-400 text-[10px] font-bold uppercase tracking-[0.2em] mt-1">
            Manage all your core workspace modules
          </p>
        </div>
        {/* You can trigger your NewProjectModal here too */}
      </header>

      <ProjectFilters 
        searchQuery={searchQuery} 
        setSearchQuery={setSearchQuery}
        activeStatus={activeStatus}
        setActiveStatus={setActiveStatus}
        sortBy={sortBy}
        setSortBy={setSortBy}
      />

      {/* ✅ Pass handleDelete to ProjectTable */}
      <ProjectTable projects={filteredAndSorted} onDelete={handleDelete} />
    </div>
  );
}
