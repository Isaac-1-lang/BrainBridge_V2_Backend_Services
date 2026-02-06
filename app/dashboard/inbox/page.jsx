"use client";
import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'next/navigation';
import { Send, Hash, Paperclip, Search } from 'lucide-react';

export default function InboxPage() {
  const searchParams = useSearchParams();
  const [chats, setChats] = useState([
    { id: 'global', name: "Global Chat", project: null, lastMsg: "System online." }
  ]);
  const [messages, setMessages] = useState([
    { id: 1, sender: "System", text: "Secure channel initialized.", project: null }
  ]);
  const [inputText, setInputText] = useState("");
  const [activeChatId, setActiveChatId] = useState('global');

  const activeChat = chats.find(c => c.id === activeChatId) || chats[0];

  useEffect(() => {
    const isNewChat = searchParams.get('newChat');
    const projectTag = searchParams.get('project');
    const recipient = searchParams.get('to');
    const draftMsg = searchParams.get('msg');

    if (isNewChat && projectTag) {
      const newChatId = `chat-${projectTag}`;
      setChats(prev => {
        if (prev.find(c => c.id === newChatId)) return prev;
        return [{ id: newChatId, name: recipient, project: projectTag, lastMsg: "Drafting..." }, ...prev];
      });
      setActiveChatId(newChatId);
      if (draftMsg) setInputText(draftMsg);
    }
  }, [searchParams]);

  const sendMessage = (e) => {
    e.preventDefault();
    if (!inputText.trim()) return;
    const newMessage = { id: Date.now(), sender: "You", text: inputText, project: activeChat.project };
    setMessages([...messages, newMessage]);
    setChats(prev => prev.map(c => c.id === activeChatId ? { ...c, lastMsg: inputText } : c));
    setInputText("");
  };

  return (
    <div className="flex h-[calc(100vh-120px)] bg-white rounded-[2.5rem] border border-gray-100 overflow-hidden shadow-sm">
      {/* Sidebar */}
      <div className="w-80 border-r border-gray-50 flex flex-col bg-white">
        <div className="p-6">
          <h2 className="text-[10px] font-black uppercase tracking-[0.2em] text-[#08075C] mb-6">Active Nodes</h2>
          <div className="relative mb-4">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={14} />
            <input 
              type="text" 
              placeholder="Search chats..." 
              className="w-full bg-gray-100 border-none rounded-xl py-2 pl-9 pr-4 text-[11px] text-[#08075C] font-bold outline-none placeholder:text-gray-400" 
            />
          </div>
        </div>
        <div className="flex-1 overflow-y-auto px-3 space-y-1">
          {chats.map((chat) => (
            <div key={chat.id} onClick={() => setActiveChatId(chat.id)} className={`p-4 rounded-2xl cursor-pointer transition-all ${activeChatId === chat.id ? 'bg-[#08075C] text-white shadow-lg shadow-blue-900/20' : 'hover:bg-gray-50 text-gray-500'}`}>
              <div className="flex items-center gap-3">
                <div className={`w-10 h-10 rounded-xl flex items-center justify-center font-bold text-xs ${activeChatId === chat.id ? 'bg-white/10 text-white' : 'bg-gray-100 text-[#08075C]'}`}>{chat.name.charAt(0)}</div>
                <div className="overflow-hidden">
                  <p className={`text-xs font-black truncate ${activeChatId === chat.id ? 'text-white' : 'text-[#08075C]'}`}>{chat.name}</p>
                  <p className="text-[9px] opacity-60 truncate font-medium uppercase tracking-tight">{chat.project ? `Proj: ${chat.project}` : chat.lastMsg}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Chat Area */}
      <div className="flex-1 flex flex-col bg-gray-50/30">
        <div className="p-6 bg-white border-b border-gray-50 flex justify-between items-center">
          <h3 className="text-sm font-black text-[#08075C]">{activeChat.name}</h3>
          {activeChat.project && (
            <div className="flex items-center gap-2 px-4 py-1.5 bg-[#3A38DE] rounded-full text-white text-[9px] font-black uppercase tracking-widest shadow-md">
              <Hash size={12} /> {activeChat.project}
            </div>
          )}
        </div>
        <div className="flex-1 overflow-y-auto p-8 space-y-6">
          {messages.filter(m => !m.project || m.project === activeChat.project).map((msg) => (
            <div key={msg.id} className={`flex flex-col ${msg.sender === "You" ? "items-end" : "items-start"}`}>
              <div className={`max-w-[70%] p-4 rounded-2xl text-xs leading-relaxed shadow-sm ${msg.sender === "You" ? "bg-[#08075C] text-white rounded-tr-none" : "bg-white text-[#08075C] border border-gray-100 rounded-tl-none font-semibold"}`}>
                {msg.text}
              </div>
              <span className="text-[8px] font-black uppercase text-gray-300 mt-2 tracking-widest">{msg.sender}</span>
            </div>
          ))}
        </div>
        <div className="p-6 bg-white border-t border-gray-50">
          <form onSubmit={sendMessage} className="relative">
            <input 
              type="text"
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
              placeholder="Type your message..."
              className="w-full bg-gray-50 border border-gray-200 rounded-2xl pl-6 pr-28 py-4 text-sm text-[#08075C] font-semibold outline-none focus:ring-4 focus:ring-[#3A38DE]/5 transition-all placeholder:text-gray-400"
            />
            <div className="absolute right-3 top-1/2 -translate-y-1/2 flex items-center gap-2">
              <button type="button" className="p-2 text-gray-300"><Paperclip size={18} /></button>
              <button type="submit" className="bg-[#3A38DE] text-white px-5 py-2.5 rounded-xl hover:bg-[#08075C] transition-all flex items-center gap-2">
                <span className="text-[9px] font-black uppercase tracking-widest">Send</span>
                <Send size={14} />
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}