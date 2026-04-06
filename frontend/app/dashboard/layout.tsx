import { auth } from "@/auth";
import { AppShell } from "@/components/dashboard/app-shell";
import { redirect } from "next/navigation";

export default async function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const session = await auth();
  if (!session?.user) {
    redirect("/login");
  }

  const userName =
    session.user.name ??
    session.user.email?.split("@")[0] ??
    "Operator";

  return (
    <AppShell userName={userName} userEmail={session.user.email}>
      <main className="px-8 pb-12 pt-6">{children}</main>
    </AppShell>
  );
}
