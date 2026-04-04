import Link from "next/link";

const navMain = [
  { href: "/dashboard", label: "Painel", icon: "grid_view", active: true },
  {
    href: "/dashboard/solicitacoes",
    label: "Solicitações",
    icon: "assignment",
    soon: true,
  },
  {
    href: "/dashboard/catalogo",
    label: "Catálogo de serviços",
    icon: "menu_book",
    soon: true,
  },
  {
    href: "/dashboard/cidadaos",
    label: "Cidadãos",
    icon: "groups",
    soon: true,
  },
  {
    href: "/dashboard/relatorios",
    label: "Relatórios",
    icon: "analytics",
    soon: true,
  },
];

const navSecondary = [
  { href: "/dashboard/equipe", label: "Equipe", icon: "person", soon: true },
  { href: "/dashboard/mensagens", label: "Mensagens", icon: "mail", soon: true },
];

export function AppSidebar() {
  return (
    <aside className="fixed left-0 top-0 z-20 flex h-screen w-64 flex-col overflow-y-auto custom-scrollbar border-r border-gray-200 bg-white px-4 py-6">
      <Link
        href="/dashboard"
        className="mb-8 px-4 text-2xl font-black tracking-tighter text-on-surface"
      >
        All Citizens
      </Link>
      <nav className="flex flex-1 flex-col space-y-1">
        {navMain.map((item) =>
          item.soon ? (
            <div
              key={item.label}
              title="Em breve"
              className="flex cursor-not-allowed items-center gap-3 rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
            >
              <span className="material-symbols-outlined text-[20px]">
                {item.icon}
              </span>
              <span className="text-sm font-medium">{item.label}</span>
            </div>
          ) : (
            <Link
              key={item.href}
              href={item.href}
              className="flex items-center justify-between rounded-lg bg-gray-100 px-4 py-2.5 text-on-surface transition-all"
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px]">
                  {item.icon}
                </span>
                <span className="text-sm font-semibold">{item.label}</span>
              </div>
            </Link>
          ),
        )}
        <div className="mt-4 space-y-1 border-t border-gray-100 pt-4">
          {navSecondary.map((item) => (
            <div
              key={item.label}
              title="Em breve"
              className="flex cursor-not-allowed items-center gap-3 rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
            >
              <span className="material-symbols-outlined text-[20px]">
                {item.icon}
              </span>
              <span className="text-sm font-medium">{item.label}</span>
            </div>
          ))}
          <div
            title="Em breve"
            className="flex cursor-not-allowed items-center justify-between rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
          >
            <div className="flex items-center gap-3">
              <span className="material-symbols-outlined text-[20px]">
                notifications
              </span>
              <span className="text-sm font-medium">Notificações</span>
            </div>
            <span className="rounded-full bg-gray-200 px-1.5 py-0.5 text-[10px] text-on-surface-variant">
              —
            </span>
          </div>
        </div>
      </nav>
      <div className="space-y-1 pt-6">
        <div
          title="Em breve"
          className="flex cursor-not-allowed items-center justify-between rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
        >
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-[20px]">
              settings
            </span>
            <span className="text-sm font-medium">Configurações</span>
          </div>
          <span className="material-symbols-outlined text-sm">expand_more</span>
        </div>
        <div
          title="Em breve"
          className="flex cursor-not-allowed items-center gap-3 rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
        >
          <span className="material-symbols-outlined text-[20px]">help</span>
          <span className="text-sm font-medium">Ajuda</span>
        </div>
      </div>
    </aside>
  );
}
