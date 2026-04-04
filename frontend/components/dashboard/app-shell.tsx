import { AppHeader } from "@/components/dashboard/app-header";
import { AppSidebar } from "@/components/dashboard/app-sidebar";

type Props = {
  userName: string;
  userEmail?: string | null;
  children: React.ReactNode;
};

export function AppShell({ userName, userEmail, children }: Props) {
  return (
    <div className="min-h-screen bg-app-bg text-on-surface antialiased">
      <AppSidebar />
      <AppHeader userName={userName} userEmail={userEmail} />
      <div className="ml-64 min-h-screen pt-16">{children}</div>
    </div>
  );
}
