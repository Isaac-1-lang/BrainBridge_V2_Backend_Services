"use client";

import React, { useState, useEffect } from 'react';
import Link from 'next/link';

export default function SignUpPage() {
  const [errors, setErrors] = useState({});
  const [formData, setFormData] = useState({
    username: '', firstName: '', lastName: '', email: '', password: '', confirmPassword: ''
  });

  const [requirements, setRequirements] = useState({
    length: false, upper: false, number: false, special: false
  });

  useEffect(() => {
    const pass = formData.password;
    setRequirements({
      length: pass.length >= 8,
      upper: /[A-Z]/.test(pass),
      number: /[0-9]/.test(pass),
      special: /[^A-Za-z0-9]/.test(pass)
    });
  }, [formData.password]);

  const handleSignUp = () => {
    let newErrors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!formData.email) newErrors.email = "Email is required";
    else if (!emailRegex.test(formData.email)) newErrors.email = "Invalid email format";

    ['username', 'firstName', 'lastName'].forEach(field => {
      if (!formData[field].trim()) newErrors[field] = "Required";
    });

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    setErrors(newErrors);
    if (Object.keys(newErrors).length === 0) alert("Account Ready!");
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

        /* Blueprint Grid Background - Using very light brand blue for lines */
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

        /* Arcs using the Royal Blue (#3A38DE) with low opacity */
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

        /* Main Glass Card */
        .main-card {
          position: relative;
          z-index: 10;
          background: rgba(255, 255, 255, 0.96);
          backdrop-filter: blur(8px);
          border: 1px solid rgba(8, 7, 92, 0.1);
          border-radius: 2.5rem;
          width: 100%;
          max-width: 1050px;
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

        /* Button Gradient using consistent brand colors */
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
          
          {/* LEFT SIDE: FORM */}
          <div className="w-full lg:w-1/2 p-8 sm:p-12 md:p-16">
            <h1 className="text-3xl font-bold mb-6 bg-clip-text text-transparent w-fit"
              style={{ fontFamily: "'Playpen Sans', cursive", backgroundImage: "linear-gradient(90deg, #08075C, #3A38DE)" }}>
              BrainBridge
            </h1>

            <h2 className="text-3xl font-bold mb-1 text-[#08075C]">Create an account</h2>
            <p className="text-sm text-gray-400 mb-8 font-medium">
              Join the developer hub. Already a member? <Link href="/auth/login" className="text-[#3A38DE] font-bold hover:underline">Login</Link>
            </p>

            <div className="space-y-4">
              <div>
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Username</label>
                <input type="text" value={formData.username} onChange={(e) => handleChange(e, 'username')}
                  className={`input-field w-full rounded-2xl px-5 py-3.5 outline-none ${errors.username ? 'error-ring animate-shake' : ''}`} />
              </div>

              <div className="flex gap-4">
                <div className="flex-1">
                  <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">First Name</label>
                  <input type="text" value={formData.firstName} onChange={(e) => handleChange(e, 'firstName')}
                    className="input-field w-full rounded-2xl px-5 py-3.5 outline-none" />
                </div>
                <div className="flex-1">
                  <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Last Name</label>
                  <input type="text" value={formData.lastName} onChange={(e) => handleChange(e, 'lastName')}
                    className="input-field w-full rounded-2xl px-5 py-3.5 outline-none" />
                </div>
              </div>

              <div>
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">E-mail</label>
                <input type="email" value={formData.email} onChange={(e) => handleChange(e, 'email')}
                  placeholder="name@email.com"
                  className={`input-field w-full rounded-2xl px-5 py-3.5 outline-none ${errors.email ? 'error-ring animate-shake' : ''}`} />
              </div>

              <div>
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Password</label>
                <input type="password" value={formData.password} onChange={(e) => handleChange(e, 'password')}
                  className="input-field w-full rounded-2xl px-5 py-3.5 outline-none" />
                <div className="grid grid-cols-2 gap-2 mt-3 px-1">
                  <Requirement label="8+ Chars" met={requirements.length} />
                  <Requirement label="Uppercase" met={requirements.upper} />
                  <Requirement label="One Number" met={requirements.number} />
                  <Requirement label="Special" met={requirements.special} />
                </div>
              </div>

              <div className="pb-2">
                <label className="text-[10px] font-bold text-gray-400 uppercase tracking-widest ml-1">Confirm Password</label>
                <input type="password" value={formData.confirmPassword} onChange={(e) => handleChange(e, 'confirmPassword')}
                  className={`input-field w-full rounded-2xl px-5 py-3.5 outline-none ${errors.confirmPassword ? 'error-ring animate-shake' : ''}`} />
                {errors.confirmPassword && <span className="text-[10px] text-red-500 font-bold ml-1">{errors.confirmPassword}</span>}
              </div>

              <Link href="/dashboard" className="btn-brand w-full text-white py-4 rounded-2xl font-bold text-lg shadow-xl active:scale-95 transition-all inline-block text-center">
                Create Account
              </Link>
            </div>
          </div>

          {/* RIGHT SIDE: BRAND SHOWCASE */}
          <div className="hidden lg:flex w-1/2 bg-[#F8F9FF] relative items-center justify-center p-16">
            <div className="relative z-20 text-center">
               <div className="w-32 h-32 bg-white rounded-[2.5rem] flex items-center justify-center shadow-xl mb-10 mx-auto border border-blue-50">
                  <i className="fa-solid fa-code-branch text-5xl text-[#3A38DE] opacity-40"></i>
               </div>
               <h3 className="text-3xl font-bold text-[#08075C] mb-4 leading-tight">
                 Build. Showcase.<br/>Connect.
               </h3>
               <p className="text-[#3A38DE] font-semibold max-w-xs mx-auto mb-8 leading-relaxed opacity-80">
                 The premium hub where developers transform code into visibility.
               </p>
               <div className="flex justify-center gap-2">
                  <span className="w-12 h-2 bg-[#08075C] rounded-full"></span>
                  <span className="w-2 h-2 bg-[#3A38DE] opacity-20 rounded-full"></span>
                  <span className="w-2 h-2 bg-[#3A38DE] opacity-20 rounded-full"></span>
               </div>
            </div>
            
            {/* Background Accent for right side */}
            <div className="absolute inset-0 bg-gradient-to-br from-transparent to-blue-50/50"></div>
          </div>

        </div>
      </div>
    </>
  );
}

function Requirement({ label, met }) {
  return (
    <div className="flex items-center gap-2">
      <div className={`w-3.5 h-3.5 rounded-full flex items-center justify-center transition-all ${met ? 'bg-green-500 scale-110' : 'bg-gray-200'}`}>
        {met && <i className="fa-solid fa-check text-[7px] text-white"></i>}
      </div>
      <span className={`text-[10px] font-bold ${met ? 'text-green-600' : 'text-gray-400'}`}>{label}</span>
    </div>
  );
}