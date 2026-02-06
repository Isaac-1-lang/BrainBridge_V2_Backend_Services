"use client";
import React, { useState } from 'react';

export default function NewProjectModal({ isOpen, onClose, onAddProject }) {
  // --- Form State ---
  const [preview, setPreview] = useState(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('Idea');
  const [category, setCategory] = useState('Web App'); // New State
  const [github, setGithub] = useState('');
  const [projectType, setProjectType] = useState('Personal');

  // --- Dynamic Lists State ---
  const [skills, setSkills] = useState(['React', 'Tailwind']);
  const [skillInput, setSkillInput] = useState('');
  const [members, setMembers] = useState([]);
  const [memberInput, setMemberInput] = useState('');

  // --- Handlers ---
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) setPreview(URL.createObjectURL(file));
  };

  const handleSkillKeyDown = (e) => {
    if (e.key === 'Enter' || e.key === ',') {
      e.preventDefault();
      const val = skillInput.trim().replace(',', '');
      if (val && !skills.includes(val)) {
        setSkills([...skills, val]);
        setSkillInput('');
      }
    } else if (e.key === 'Backspace' && !skillInput && skills.length > 0) {
      setSkills(skills.slice(0, -1));
    }
  };

  const handleAddMember = (e) => {
    if (e) e.preventDefault();
    const val = memberInput.trim();
    if (val && !members.includes(val)) {
      setMembers([...members, val]);
      setMemberInput('');
    }
  };

  const handleClose = () => {
    setPreview(null); setName(''); setDescription('');
    setStatus('Idea'); setGithub(''); setProjectType('Personal');
    setCategory('Web App'); setSkills(['React', 'Tailwind']); setMembers([]);
    onClose();
  };

  const handleSubmit = () => {
    if (!name.trim() || !description.trim()) {
      alert("Please provide a project name and value proposition.");
      return;
    }
    if (status !== 'Idea' && !github.trim()) {
      alert("Please provide a GitHub repository link for this phase.");
      return;
    }

    onAddProject({
      name, description, status, category, github, projectType,
      tech: skills, members, image: preview,
      createdAt: new Date().toISOString()
    });
    handleClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-[#08075C]/10 backdrop-blur-[2px]">
      <div className="bg-white w-full max-w-xl rounded-2xl shadow-2xl border border-gray-100 overflow-hidden animate-in fade-in zoom-in duration-200">
        
        {/* Header */}
        <div className="px-6 py-4 border-b border-gray-50 flex justify-between items-center bg-white">
          <div>
            <h3 className="text-sm font-black text-[#08075C] uppercase tracking-widest">Project Configuration</h3>
            <p className="text-[10px] text-gray-400 font-medium italic">Define your innovation's core parameters</p>
          </div>
          <button onClick={handleClose} className="text-gray-400 hover:text-red-500 transition-colors">
            <i className="fa-solid fa-xmark text-sm"></i>
          </button>
        </div>

        <div className="p-6 space-y-5 max-h-[70vh] overflow-y-auto custom-scrollbar">
          
          {/* 1. Image Upload */}
          <div className="flex items-center gap-4">
            <div className="w-24 h-24 rounded-xl border-2 border-dashed border-gray-100 bg-gray-50 flex-shrink-0 overflow-hidden relative group">
              {preview ? (
                <>
                  <img src={preview} alt="Preview" className="w-full h-full object-cover" />
                  <button onClick={() => setPreview(null)} className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 flex items-center justify-center text-white text-xs transition-opacity">
                    <i className="fa-solid fa-trash-can text-sm"></i>
                  </button>
                </>
              ) : (
                <div className="w-full h-full flex flex-col items-center justify-center text-gray-300">
                  <i className="fa-solid fa-cloud-arrow-up text-lg mb-1"></i>
                  <span className="text-[8px] font-bold uppercase">Cover</span>
                </div>
              )}
            </div>
            <div className="flex-1">
              <label className="cursor-pointer bg-white border border-gray-200 px-4 py-2 rounded-lg text-[10px] font-bold text-[#08075C] hover:border-[#3A38DE] transition-all">
                Select Graphic
                <input type="file" className="hidden" accept="image/*" onChange={handleImageChange} />
              </label>
              <p className="text-[9px] text-gray-400 mt-2 font-medium">PNG, JPG or WEBP. Max 2MB.</p>
            </div>
          </div>

          <div className="h-px bg-gray-50 w-full"></div>

          {/* 2. Basic Info */}
          <div className="space-y-4">
            <div>
              <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-1.5 ml-1">Project Name</label>
              <input 
                type="text" value={name} onChange={(e) => setName(e.target.value)}
                className="w-full bg-gray-50 border border-gray-100 rounded-xl py-2.5 px-4 text-xs font-semibold outline-none focus:border-[#3A38DE] text-[#08075C] transition-all" 
                placeholder="Project Title"
              />
            </div>
            <div>
              <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-1.5 ml-1">Value Proposition</label>
              <textarea 
                rows="3" value={description} onChange={(e) => setDescription(e.target.value)}
                className="w-full bg-gray-50 border border-gray-100 rounded-xl py-2.5 px-4 text-xs outline-none focus:border-[#3A38DE] text-[#08075C] transition-all resize-none leading-relaxed overflow-hidden" 
                style={{ display: '-webkit-box', WebkitLineClamp: '3', WebkitBoxOrient: 'vertical' }}
                placeholder="Describe the core innovation (3 lines max)..."
              />
            </div>
          </div>

          {/* 3. Category Selection */}
          <div>
            <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-2 ml-1">Innovation Category</label>
            <div className="flex flex-wrap gap-2">
              {['Web App', 'Mobile', 'AI/ML', 'Design', 'Blockchain'].map((cat) => (
                <button
                  key={cat}
                  onClick={() => setCategory(cat)}
                  className={`px-3 py-1.5 rounded-lg text-[9px] font-bold border transition-all ${
                    category === cat 
                    ? 'bg-[#3A38DE] border-[#3A38DE] text-white shadow-md shadow-blue-100' 
                    : 'bg-white border-gray-100 text-gray-400 hover:border-gray-300'
                  }`}
                >
                  {cat}
                </button>
              ))}
            </div>
          </div>

          {/* 4. Toggles & Repository */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-1.5 ml-1">Phase</label>
              <select 
                value={status} onChange={(e) => setStatus(e.target.value)}
                className="w-full bg-gray-50 border border-gray-100 rounded-xl py-2.5 px-3 text-[11px] font-bold text-[#08075C] outline-none cursor-pointer"
              >
                <option value="Idea">Idea Phase</option>
                <option value="In Progress">In Progress</option>
                <option value="Completed">Completed</option>
                <option value="Stopped">Stopped</option>
              </select>
            </div>
            <div>
              <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-1.5 ml-1">Type</label>
              <div className="flex bg-gray-50 p-1 rounded-xl border border-gray-100">
                <button 
                  onClick={() => setProjectType('Personal')}
                  className={`flex-1 py-1.5 rounded-lg text-[10px] font-bold transition-all ${projectType === 'Personal' ? 'bg-white shadow-sm text-[#3A38DE]' : 'text-gray-400'}`}
                >Personal</button>
                <button 
                  onClick={() => setProjectType('Team')}
                  className={`flex-1 py-1.5 rounded-lg text-[10px] font-bold transition-all ${projectType === 'Team' ? 'bg-white shadow-sm text-[#3A38DE]' : 'text-gray-400'}`}
                >Team</button>
              </div>
            </div>
          </div>

          {status !== 'Idea' && (
            <div className="animate-in slide-in-from-top-2 duration-300">
              <label className="block text-[10px] font-black text-[#3A38DE] uppercase tracking-wider mb-1.5 ml-1">Source Repository</label>
              <div className="relative">
                <i className="fa-brands fa-github absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm"></i>
                <input 
                  type="url" value={github} onChange={(e) => setGithub(e.target.value)}
                  className="w-full bg-blue-50/30 border border-blue-100 rounded-xl py-2.5 pl-11 pr-4 text-xs outline-none focus:border-[#3A38DE] text-[#08075C] font-medium" 
                  placeholder="https://github.com/user/repo"
                />
              </div>
            </div>
          )}

          {/* 5. Modern Skills Tag Input */}
          <div className="space-y-2">
            <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider mb-1.5 ml-1">Technologies & Skills</label>
            <div className="flex flex-wrap gap-2 p-2.5 bg-gray-50 border border-gray-100 rounded-xl focus-within:border-[#3A38DE] focus-within:bg-white transition-all min-h-[46px]">
              {skills.map(tag => (
                <span key={tag} className="flex items-center gap-1.5 bg-[#3A38DE] text-white px-2 py-1 rounded-md text-[9px] font-bold shadow-sm">
                  {tag} 
                  <button type="button" onClick={() => setSkills(skills.filter(t => t !== tag))}>
                    <i className="fa-solid fa-xmark text-[7px] hover:text-red-300 transition-colors"></i>
                  </button>
                </span>
              ))}
              <input 
                value={skillInput} 
                onChange={(e) => setSkillInput(e.target.value)}
                onKeyDown={handleSkillKeyDown}
                className="bg-transparent text-[11px] outline-none font-bold text-[#08075C] placeholder-gray-400 flex-grow min-w-[100px]" 
                placeholder={skills.length === 0 ? "+ Add Skill..." : ""}
              />
            </div>
          </div>

          {/* 6. Team Tagging */}
          {projectType === 'Team' && (
            <div className="animate-in slide-in-from-top-2 duration-300 space-y-2">
              <label className="block text-[10px] font-black text-gray-400 uppercase tracking-wider ml-1">Collaborators</label>
              <div className="flex flex-wrap gap-2 p-2 bg-gray-50/50 rounded-lg min-h-[30px]">
                {members.map(m => (
                  <span key={m} className="bg-white text-[#08075C] px-2 py-1 rounded-md text-[9px] font-bold flex items-center gap-2 border border-gray-200">
                    @{m} <i onClick={() => setMembers(members.filter(i => i !== m))} className="fa-solid fa-xmark text-[8px] cursor-pointer hover:text-red-500"></i>
                  </span>
                ))}
              </div>
              <div className="flex gap-2">
                <input 
                  value={memberInput} 
                  onChange={(e) => setMemberInput(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && (e.preventDefault(), handleAddMember())}
                  className="flex-1 bg-gray-50 border border-gray-100 rounded-xl py-2 px-4 text-xs outline-none focus:border-[#3A38DE] text-[#08075C]" 
                  placeholder="Username..."
                />
                <button 
                  onClick={handleAddMember}
                  className="bg-[#08075C] text-white px-4 rounded-xl text-[10px] font-black hover:bg-[#3A38DE] transition-all"
                >TAG</button>
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end items-center gap-4">
          <button onClick={handleClose} className="text-[10px] font-bold text-gray-400 hover:text-red-500 uppercase tracking-widest transition-colors">
            Discard
          </button>
          <button 
            onClick={handleSubmit}
            className="bg-[#3A38DE] text-white px-8 py-2.5 rounded-xl text-[10px] font-black uppercase tracking-widest shadow-xl shadow-blue-100 hover:scale-[1.02] active:scale-[0.98] transition-all"
          >
            Add Project
          </button>
        </div>
      </div>
    </div>
  );
}