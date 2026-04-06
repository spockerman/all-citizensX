"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";

const navMain = [
  { href: "/dashboard", label: "Dashboard", icon: "grid_view" as const },
  {
    href: "/dashboard/service-requests",
    label: "Service requests",
    icon: "assignment" as const,
  },
  {
    href: "/dashboard/subjects",
    label: "Subjects",
    icon: "topic" as const,
  },
  {
    href: "/dashboard/service-catalog",
    label: "Service catalog",
    icon: "menu_book" as const,
    soon: true,
  },
  {
    href: "/dashboard/citizens",
    label: "Citizens",
    icon: "groups" as const,
    soon: true,
  },
  {
    href: "/dashboard/reports",
    label: "Reports",
    icon: "analytics" as const,
    soon: true,
  },
];

const navSecondary = [
  { href: "/dashboard/team", label: "Team", icon: "person", soon: true },
  { href: "/dashboard/messages", label: "Messages", icon: "mail", soon: true },
];

function navItemIsActive(pathname: string, href: string) {
  if (href === "/dashboard") return pathname === "/dashboard";
  return pathname === href || pathname.startsWith(`${href}/`);
}

export function AppSidebar() {
  const pathname = usePathname();

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
              title="Coming soon"
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
              className={
                navItemIsActive(pathname, item.href)
                  ? "flex items-center justify-between rounded-lg bg-gray-100 px-4 py-2.5 text-on-surface transition-all"
                  : "flex items-center justify-between rounded-lg px-4 py-2.5 text-on-surface transition-all hover:bg-gray-50"
              }
            >
              <div className="flex items-center gap-3">
                <span className="material-symbols-outlined text-[20px]">
                  {item.icon}
                </span>
                <span
                  className={
                    navItemIsActive(pathname, item.href)
                      ? "text-sm font-semibold"
                      : "text-sm font-medium"
                  }
                >
                  {item.label}
                </span>
              </div>
            </Link>
          ),
        )}
        <div className="mt-4 space-y-1 border-t border-gray-100 pt-4">
          {navSecondary.map((item) => (
            <div
              key={item.label}
              title="Coming soon"
              className="flex cursor-not-allowed items-center gap-3 rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
            >
              <span className="material-symbols-outlined text-[20px]">
                {item.icon}
              </span>
              <span className="text-sm font-medium">{item.label}</span>
            </div>
          ))}
          <div
            title="Coming soon"
            className="flex cursor-not-allowed items-center justify-between rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
          >
            <div className="flex items-center gap-3">
              <span className="material-symbols-outlined text-[20px]">
                notifications
              </span>
              <span className="text-sm font-medium">Notifications</span>
            </div>
            <span className="rounded-full bg-gray-200 px-1.5 py-0.5 text-[10px] text-on-surface-variant">
              —
            </span>
          </div>
        </div>
      </nav>
      <div className="space-y-1 pt-6">
        <div
          title="Coming soon"
          className="flex cursor-not-allowed items-center justify-between rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
        >
          <div className="flex items-center gap-3">
            <span className="material-symbols-outlined text-[20px]">
              settings
            </span>
            <span className="text-sm font-medium">Settings</span>
          </div>
          <span className="material-symbols-outlined text-sm">expand_more</span>
        </div>
        <div
          title="Coming soon"
          className="flex cursor-not-allowed items-center gap-3 rounded-lg px-4 py-2.5 text-on-surface-variant opacity-55"
        >
          <span className="material-symbols-outlined text-[20px]">help</span>
          <span className="text-sm font-medium">Help</span>
        </div>
      </div>
    </aside>
  );
}
