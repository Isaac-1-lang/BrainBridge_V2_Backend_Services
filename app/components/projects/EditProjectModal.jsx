"use client";
import React, { useState, useEffect } from 'react';

export default function EditProjectModal({ isOpen, onClose, onUpdateProject, project }) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('');
  const [category, setCategory] = useState('');
  const [github, setGithub] = useState('');
  const [image, setImage] = useState(''); // New state for image

  // Sync state when modal opens with existing project data
  useEffect(() => {
    if (project && isOpen) {
      setName(project.name || '');
      setDescription(project.description || '');
      setStatus(project.status || 'Idea');
      setCategory(project.category || 'Web App');
      setGithub(project.github || '');
      setImage(project.image || ''); // Sync existing image
    }
  }, [project, isOpen]);

  // Handle image selection and conversion to Base64
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setImage(reader.result); // This saves the image string to state
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onUpdateProject({
      ...project,
      name,
      description,
      status,
      category,
      github,
      image // Pass the updated image back
    });
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[150] flex items-center justify-center p-4 bg-[#08075C]/40 backdrop-blur-md">
      <div className="bg-white w-full max-w-lg rounded-[2.5rem] shadow-2xl overflow-hidden border border-gray-100 animate-in fade-in zoom-in duration-200">
        <div className="p-10 space-y-6">
          <div className="flex justify-between items-center">
            <div>
              <h3 className="text-sm font-black text-[#08075C] uppercase tracking-widest">Update Configuration</h3>
              <p className="text-[10px] text-gray-400 font-medium italic">Modifying {project.name}</p>
            </div>
            <button onClick={onClose} className="text-gray-300 hover:text-red-500 transition-colors">
              <i className="fa-solid fa-xmark text-lg"></i>
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Image Upload Section - Keeping UI minimal */}
            <div className="flex items-center gap-4 p-3 bg-gray-50 rounded-2xl border border-gray-100">
              <div className="w-12 h-12 rounded-lg bg-gray-200 overflow-hidden flex-shrink-0 border border-gray-200">
                {image ? (
                  <img src={image} alt="Preview" className="w-full h-full object-cover" />
                ) : (
                  <div className="w-full h-full flex items-center justify-center text-gray-400">
                    <i className="fa-solid fa-image text-xs"></i>
                  </div>
                )}
              </div>
              <div className="flex-1">
                <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-1">Visual Asset</label>
                <label className="text-[10px] font-bold text-[#3A38DE] cursor-pointer hover:underline">
                  Change Image
                  <input type="file" accept="image/*" onChange={handleImageChange} className="hidden" />
                </label>
              </div>
            </div>

            <div>
              <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Project Name</label>
              <input 
                value={name} onChange={(e) => setName(e.target.value)}
                className="w-full bg-gray-50 border border-gray-100 rounded-xl py-3 px-4 text-xs font-bold text-[#08075C] outline-none focus:ring-2 focus:ring-[#3A38DE]/20"
              />
            </div>
            
            <div>
              <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Value Proposition</label>
              <textarea 
                value={description} onChange={(e) => setDescription(e.target.value)}
                className="w-full bg-gray-50 border border-gray-100 rounded-xl py-3 px-4 text-xs font-medium text-[#08075C] outline-none h-24 resize-none leading-relaxed"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Current Phase</label>
                <select 
                  value={status} onChange={(e) => setStatus(e.target.value)}
                  className="w-full bg-gray-50 border border-gray-100 rounded-xl py-3 px-4 text-xs font-bold text-[#08075C] outline-none cursor-pointer"
                >
                  <option value="Idea">Idea</option>
                  <option value="In Progress">In Progress</option>
                  <option value="Completed">Completed</option>
                  <option value="Stopped">Stopped</option>
                </select>
              </div>
              <div>
                <label className="block text-[9px] font-black text-gray-400 uppercase tracking-widest mb-2 ml-1">Category</label>
                <select 
                  value={category} onChange={(e) => setCategory(e.target.value)}
                  className="w-full bg-gray-50 border border-gray-100 rounded-xl py-3 px-4 text-xs font-bold text-[#08075C] outline-none cursor-pointer"
                >
                  {['Web App', 'Mobile', 'AI/ML', 'Design', 'Blockchain'].map(c => <option key={c} value={c}>{c}</option>)}
                </select>
              </div>
            </div>

            {status !== 'Idea' && (
              <div>
                <label className="block text-[9px] font-black text-[#3A38DE] uppercase tracking-widest mb-2 ml-1">GitHub Repository</label>
                <input 
                  value={github} onChange={(e) => setGithub(e.target.value)}
                  className="w-full bg-blue-50/50 border border-blue-100 rounded-xl py-3 px-4 text-xs font-medium text-[#08075C] outline-none"
                  placeholder="https://github.com/..."
                />
              </div>
            )}

            <button 
              type="submit"
              className="w-full bg-[#08075C] hover:bg-[#3A38DE] text-white py-4 rounded-2xl text-[10px] font-black uppercase tracking-[0.2em] shadow-xl transition-all mt-4"
            >
              Update
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}