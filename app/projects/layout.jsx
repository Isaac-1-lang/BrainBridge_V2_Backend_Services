"use client";
import Sidebar from '../components/layout/Sidebar';
import TopBar from '../components/layout/TopBar';

export default function DashboardLayout({ children }) {
  return (
    <div className="min-h-screen bg-white">
      {/* Blueprint Grid Background */}
      <div className="fixed inset-0 pointer-events-none opacity-40 z-0" 
        style={{
          backgroundImage: `linear-gradient(rgba(58, 56, 222, 0.05) 1px, transparent 1px), 
                            linear-gradient(90deg, rgba(58, 56, 222, 0.05) 1px, transparent 1px)`,
          backgroundSize: '45px 45px'
        }}>
      </div>

      <Sidebar />
      
      {/* Main Content Area */}
      <div className="ml-64 flex flex-col min-h-screen relative z-10">
        <TopBar />
        <main className="p-8 flex-1">
          {children}
        </main>
      </div>
    </div>
  );
}