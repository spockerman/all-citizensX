"use client";

import { signOut } from "next-auth/react";

function initialsFrom(name: string, email?: string | null) {
  const base = name || email || "?";
  const parts = base.split(/[\s@._-]+/).filter(Boolean);
  if (parts.length >= 2) {
    return (parts[0][0] + parts[1][0]).toUpperCase().slice(0, 2);
  }
  return base.slice(0, 2).toUpperCase();
}

type Props = {
  userName: string;
  userEmail?: string | null;
};

export function AppHeader({ userName, userEmail }: Props) {
  const initials = initialsFrom(userName, userEmail);

  return (
    <header className="fixed left-64 right-0 top-0 z-10 flex h-16 items-center justify-between border-b border-gray-100 bg-white/80 px-8 backdrop-blur-md">
      <div className="flex flex-1 items-center gap-4">
        <div className="flex w-full max-w-md items-center">
          <span className="material-symbols-outlined mr-2 text-gray-400">
            search
          </span>
          <input
            className="w-full border-none bg-transparent text-sm placeholder:text-gray-400 focus:ring-0"
            placeholder="Buscar protocolo, cidadão ou serviço…"
            type="search"
            disabled
            title="Integração com a API em breve"
          />
        </div>
      </div>
      <div className="flex items-center gap-4">
        <button
          type="button"
          className="hidden items-center gap-2 rounded-lg border border-gray-200 px-3 py-1.5 text-sm font-medium text-gray-600 transition-colors hover:bg-gray-50 md:flex"
          title="Em breve"
          disabled
        >
          <span className="material-symbols-outlined text-sm">tune</span>
          Personalizar painel
        </button>
        <div className="flex items-center gap-3">
          <div
            className="flex h-8 w-8 items-center justify-center rounded-full bg-secondary/15 text-xs font-bold text-secondary"
            title={userEmail ?? userName}
          >
            {initials}
          </div>
          <div className="hidden sm:block">
            <div className="text-sm font-semibold text-on-surface">
              {userName}
            </div>
            {userEmail ? (
              <div className="max-w-[200px] truncate text-xs text-gray-500">
                {userEmail}
              </div>
            ) : null}
          </div>
          <button
            type="button"
            onClick={() => signOut({ callbackUrl: "/login" })}
            className="rounded-lg border border-gray-200 px-3 py-1.5 text-xs font-semibold text-gray-600 hover:bg-gray-50"
          >
            Sair
          </button>
        </div>
      </div>
    </header>
  );
}
