"use client";
import React, { useState, useEffect } from "react";

export default function PublicHero({ projects }) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [fade, setFade] = useState(true);

  useEffect(() => {
    if (!projects || projects.length === 0) return;

    projects.forEach(p => {
      if (p.image) {
        const img = new Image();
        img.src = p.image;
      }
    });

    const interval = setInterval(() => {
      setFade(false);
      setTimeout(() => {
        setCurrentIndex((prev) => (prev + 1) % projects.length);
        setFade(true);
      }, 600);
    }, 7000);

    return () => clearInterval(interval);
  }, [projects]);

  if (!projects || projects.length === 0) return null;
  const active = projects[currentIndex];

  return (
    <section className="relative h-[650px] w-full bg-[#04041d] overflow-hidden flex items-center">
      
    
      <div className="absolute top-0 right-0 w-2/3 h-full">
        <div className={`relative w-full h-full transition-all duration-1000 ease-in-out ${fade ? 'translate-x-0 opacity-100' : 'translate-x-12 opacity-0'}`}>
          <img
            src={active.image}
            alt=""
            className="w-full h-full object-cover"
          />
      
          <div className="absolute inset-0 bg-gradient-to-l from-transparent via-[#04041d]/20 to-[#04041d]"></div>
        </div>
      </div>

      <div className="absolute inset-0 opacity-[0.03] pointer-events-none bg-[url('https://www.transparenttextures.com/patterns/carbon-fibre.png')]"></div>

      {/* 3. Main Content Wrapper */}
      <div className="relative w-full max-w-7xl mx-auto px-6 grid grid-cols-1 lg:grid-cols-2">
        <div className={`transition-all duration-700 delay-300 ${fade ? 'translate-y-0 opacity-100' : 'translate-y-8 opacity-0'}`}>
          
          {/* Tagline */}
          <div className="flex items-center gap-4 mb-6">
            <span className="h-[1px] w-12 bg-[#3A38DE]"></span>
            <span className="text-[#3A38DE] text-[10px] font-black uppercase tracking-[0.4em]">
              Active Node Discovery
            </span>
          </div>

          {/* Typography: Using Mixed Weights for a 'Designed' feel */}
          <h1 className="text-7xl font-[900] text-white uppercase tracking-tighter leading-[0.85] mb-6 italic">
            {active.name.split(' ')[0]} <br/>
            <span className="text-transparent stroke-text" style={{ WebkitTextStroke: '1px rgba(255,255,255,0.4)' }}>
              {active.name.split(' ').slice(1).join(' ')}
            </span>
          </h1>

          <p className="text-gray-400 text-base font-medium max-w-md mb-10 leading-relaxed border-l-2 border-white/10 pl-6">
            {active.description}
          </p>

          <div className="flex items-center gap-8">
            <button className="group relative">
               <div className="absolute -inset-1 bg-[#3A38DE] blur opacity-20 group-hover:opacity-50 transition"></div>
               <div className="relative bg-white text-[#04041d] px-10 py-5 rounded-none font-black text-[11px] uppercase tracking-widest hover:bg-[#3A38DE] hover:text-white transition-all flex items-center gap-4">
                 Exploration Mode
                 <i className="fa-solid fa-arrow-right-long transition-transform group-hover:translate-x-2"></i>
               </div>
            </button>

            <div className="flex flex-col gap-1">
               <span className="text-[8px] text-gray-500 font-black uppercase tracking-widest">Architect</span>
               <div className="flex items-center gap-3">
                  <p className="text-xs text-white font-bold uppercase tracking-wider underline decoration-[#3A38DE] underline-offset-4">
                    {active.creator}
                  </p>
               </div>
            </div>
          </div>
        </div>
      </div>

      {/* 4. Scroll Indicator / Pagination (Small details that make it look professional) */}
      <div className="absolute bottom-10 left-6 flex items-center gap-4">
         <span className="text-white text-[10px] font-black tracking-widest">0{currentIndex + 1}</span>
         <div className="w-24 h-[2px] bg-white/10">
            <div 
              className="h-full bg-[#3A38DE] transition-all duration-[7000ms] ease-linear"
              style={{ width: fade ? '100%' : '0%' }}
            ></div>
         </div>
         <span className="text-gray-600 text-[10px] font-black tracking-widest">0{projects.length}</span>
      </div>
    </section>
  );
}