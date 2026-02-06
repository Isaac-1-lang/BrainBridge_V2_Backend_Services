"use client";

import React, { useState } from 'react';
import Link from 'next/link';

export default function LoginPage() {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});

  const handleLogin = () => {
    let newErrors = {};
    if (!formData.email) newErrors.email = "Email is required";
    if (!formData.password) newErrors.password = "Password is required";
    
    setErrors(newErrors);
    if (Object.keys(newErrors).length === 0) alert("Welcome back!");
  };

  const handleChange = (e, field) => {
    setFormData({ ...formData, [field]: e.target.value });
    if (errors[field]) setErrors(prev => ({ ...prev, [field]: false }));
  };

  return (
    <>
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
      <link href="https://fonts.googleapis.com/css2?family=Playpen+Sans:wght@600;700&family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet" />

      <style dangerouslySetInnerHTML={{ __html: `
        body { margin: 0; padding: 0; background: #ffffff; overflow-x: hidden; }

        /* Blueprint Grid Background - Consistent 45px grid */
        .page-wrapper {
          min-height: 100vh;
          display: flex;
          align-items: center;
          justify-content: center;
          background-image: 
            linear-gradient(rgba(58, 56, 222, 0.05) 1px, transparent 1px),
            linear-gradient(90deg, rgba(58, 56, 222, 0.05) 1px, transparent 1px);
          background-size: 45px 45px;
          position: relative;
          padding: 40px 20px;
        }

        /* Arcs using Brand Royal Blue (#3A38DE) */
        .arc-overlay {
          position: absolute;
          width: 1100px; height: 1100px;
          border-radius: 50%;
          border: 100px solid rgba(58, 56, 222, 0.03);
          right: -15%; top: -10%;
          z-index: 1;
        }

        .arc-inner {
          position: absolute;
          width: 700px; height: 700px;
          border-radius: 50%;
          border: 60px solid rgba(8, 7, 92, 0.02);
          left: -10%; bottom: -5%;
          z-index: 1;
        }

        /* Split Glass Card */
        .main-card {
          position: relative;
          z-index: 10;
          background: rgba(255, 255, 255, 0.96);
          backdrop-filter: blur(8px);
          border: 1px solid rgba(8, 7, 92, 0.1);
          border-radius: 2.5rem;
          width: 100%;
          max-width: 1000px;
          display: flex;
          overflow: hidden;
          box-shadow: 0 50px 100px -20px rgba(8, 7, 92, 0.15);
        }

        .input-field {
          background-color: #f9fafb;
          color: #9ca3af; 
          transition: all 0.2s ease;
          border: 1px solid #e5e7eb;
        }
        .input-field:focus {
          color: #000000 !important;
          background-color: #ffffff !important;
          border-color: #3A38DE !important;
          box-shadow: 0 4px 12px rgba(58, 56, 222, 0.08);
        }

        .btn-brand {
          background: linear-gradient(90deg, #08075C, #3A38DE);
          transition: all 0.3s ease;
        }
        .btn-brand:hover {
          transform: translateY(-2px);
          box-shadow: 0 12px 24px rgba(58, 56, 222, 0.3);
          filter: brightness(1.1);
        }

        @keyframes shake { 0%, 100% { transform: translateX(0); } 25% { transform: translateX(-5px); } 75% { transform: translateX(5px); } }
        .animate-shake { animation: shake 0.2s ease-in-out 0s 2; }
        .error-ring { border-color: #ef4444 !important; }
      `}} />

      <div className="page-wrapper">
        <div className="arc-overlay"></div>
        <div className="arc-inner"></div>

        <div className="main-card flex-col lg:flex-row">
          
          {/* LEFT SIDE: LOGIN FORM */}
          <div className="w-full lg:w-1/2 p-8 sm:p-12 md:p-16">
            <h1 className="text-3xl font-bold mb-10 bg-clip-text text-transparent w-fit"
              style={{ fontFamily: "'Playpen Sans', cursive", backgroundImage: "linear-gradient(90deg, #08075C, #3A38DE)" }}>
              BrainBridge
            </h1>

            <h2 className="text-3xl font-bold mb-1 text-[#08075C]">Welcome Back</h2>
            <p className="text-sm text-gray-400 mb-8 font-medium">
              Don't have an account? <Link href="/auth/signup" className="text-[#3A38DE] font-bold hover:underline">Sign Up</Link>
            </p>

            <div className="space-y-6">
              <div>
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">E-mail Address</label>
                <input type="email" value={formData.email} onChange={(e) => handleChange(e, 'email')}
                  className={`input-field w-full rounded-2xl px-5 py-3.5 outline-none mt-1 ${errors.email ? 'error-ring animate-shake' : ''}`} 
                  placeholder="name@email.com" />
              </div>

              <div>
                <div className="flex justify-between items-center ml-1">
                  <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest">Password</label>
                  <Link href="/forgot" className="text-[10px] font-bold text-[#3A38DE] hover:underline uppercase tracking-widest">Forgot?</Link>
                </div>
                <input type="password" value={formData.password} onChange={(e) => handleChange(e, 'password')}
                  className={`input-field w-full rounded-2xl px-5 py-3.5 outline-none mt-1 ${errors.password ? 'error-ring animate-shake' : ''}`} />
              </div>

              <div className="flex items-center gap-2 ml-1">
                <input type="checkbox" id="remember" className="w-4 h-4 accent-[#3A38DE] cursor-pointer" />
                <label htmlFor="remember" className="text-xs text-gray-500 font-medium cursor-pointer">Keep me logged in</label>
              </div>

              <Link href="/dashboard" className="btn-brand w-full text-white py-4 rounded-2xl font-bold text-lg shadow-xl mt-4 active:scale-95 transition-all inline-block text-center">
                Sign In
              </Link>
            </div>
          </div>

          {/* RIGHT SIDE: BRAND SHOWCASE */}
          <div className="hidden lg:flex w-1/2 bg-[#F8F9FF] relative items-center justify-center p-16">
            <div className="relative z-20 text-center">
               <div className="w-32 h-32 bg-white rounded-[2.5rem] flex items-center justify-center shadow-xl mb-10 mx-auto border border-blue-50">
                  <i className="fa-solid fa-rocket text-5xl text-[#3A38DE] opacity-40"></i>
               </div>
               <h3 className="text-3xl font-bold text-[#08075C] mb-4 leading-tight">
                 Elevate Your<br/>Engineering.
               </h3>
               <p className="text-[#3A38DE] font-semibold max-w-xs mx-auto mb-8 leading-relaxed opacity-80">
                 Login to access your personalized developer dashboard and global networking tools.
               </p>
               <div className="flex justify-center gap-2">
                  <span className="w-2 h-2 bg-[#3A38DE] opacity-20 rounded-full"></span>
                  <span className="w-12 h-2 bg-[#08075C] rounded-full"></span>
                  <span className="w-2 h-2 bg-[#3A38DE] opacity-20 rounded-full"></span>
               </div>
            </div>
            
            <div className="absolute inset-0 bg-gradient-to-br from-transparent to-blue-50/50"></div>
          </div>

        </div>
      </div>
    </>
  );
}