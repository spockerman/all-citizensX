"use client";

import type { SubjectDetail } from "@/lib/subjects-server";
import { useRouter } from "next/navigation";
import { useState } from "react";

type Props = {
  mode: "create" | "edit";
  tenantConfigured: boolean;
  initial?: SubjectDetail;
};

export function SubjectForm({ mode, tenantConfigured, initial }: Props) {
  const router = useRouter();
  const [name, setName] = useState(initial?.name ?? "");
  const [departmentId, setDepartmentId] = useState(
    initial?.departmentId ?? "",
  );
  const [visibleWeb, setVisibleWeb] = useState(initial?.visibleWeb ?? true);
  const [visibleApp, setVisibleApp] = useState(initial?.visibleApp ?? true);
  const [active, setActive] = useState(initial?.active ?? true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const canSubmit =
    mode === "create" ? tenantConfigured : tenantConfigured && Boolean(initial);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      if (mode === "create") {
        const res = await fetch("/api/subjects", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name,
            departmentId: departmentId.trim() || undefined,
            visibleWeb,
            visibleApp,
          }),
        });
        const data = (await res.json()) as { error?: string; detail?: string };
        if (!res.ok) {
          setError(
            data.detail
              ? `${data.error ?? "Error"}: ${String(data.detail).slice(0, 400)}`
              : (data.error ?? `HTTP ${res.status}`),
          );
          return;
        }
      } else if (initial) {
        const res = await fetch(
          `/api/subjects/${encodeURIComponent(initial.id)}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              name,
              departmentId: departmentId.trim() || undefined,
              visibleWeb,
              visibleApp,
              active,
            }),
          },
        );
        const data = (await res.json()) as { error?: string; detail?: string };
        if (!res.ok) {
          setError(
            data.detail
              ? `${data.error ?? "Error"}: ${String(data.detail).slice(0, 400)}`
              : (data.error ?? `HTTP ${res.status}`),
          );
          return;
        }
      }
      router.push("/dashboard/subjects");
      router.refresh();
    } catch {
      setError("Network error while submitting the form.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="mx-auto max-w-2xl space-y-6 rounded-xl border border-gray-100 bg-white p-6 md:p-8"
    >
      {!tenantConfigured ? (
        <div className="rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-950">
          Set <code className="rounded bg-amber-100/80 px-1">TENANT_ID</code> in
          the frontend environment to sync subjects with the API.
        </div>
      ) : null}

      {error ? (
        <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-900">
          {error}
        </div>
      ) : null}

      <div>
        <label
          htmlFor="subject-name"
          className="mb-2 block text-sm font-bold text-on-surface"
        >
          Name <span className="text-red-600">*</span>
        </label>
        <input
          id="subject-name"
          type="text"
          required
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="e.g. Water and sanitation"
          className="w-full rounded-lg border border-gray-200 bg-gray-50/50 px-4 py-3 text-sm text-on-surface placeholder:text-gray-400 focus:border-accent focus:ring-1 focus:ring-accent"
        />
      </div>

      <div>
        <label
          htmlFor="subject-dept"
          className="mb-2 block text-sm font-bold text-on-surface"
        >
          Department (optional UUID)
        </label>
        <input
          id="subject-dept"
          type="text"
          value={departmentId}
          onChange={(e) => setDepartmentId(e.target.value)}
          placeholder="Leave blank if not linked to a department"
          className="w-full rounded-lg border border-gray-200 bg-gray-50/50 px-4 py-3 text-sm font-mono text-on-surface placeholder:text-gray-400 focus:border-accent focus:ring-1 focus:ring-accent"
        />
      </div>

      <div className="grid gap-4 sm:grid-cols-2">
        <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-gray-100 bg-gray-50/30 px-4 py-3 text-sm font-medium text-on-surface">
          <input
            type="checkbox"
            checked={visibleWeb}
            onChange={(e) => setVisibleWeb(e.target.checked)}
            className="h-4 w-4 rounded border-gray-300 text-accent focus:ring-accent"
          />
          Visible on web portal
        </label>
        <label className="flex cursor-pointer items-center gap-3 rounded-lg border border-gray-100 bg-gray-50/30 px-4 py-3 text-sm font-medium text-on-surface">
          <input
            type="checkbox"
            checked={visibleApp}
            onChange={(e) => setVisibleApp(e.target.checked)}
            className="h-4 w-4 rounded border-gray-300 text-accent focus:ring-accent"
          />
          Visible on mobile app
        </label>
      </div>

      {mode === "edit" ? (
        <label className="flex cursor-pointer items-center gap-3 text-sm font-medium text-on-surface">
          <input
            type="checkbox"
            checked={active}
            onChange={(e) => setActive(e.target.checked)}
            className="h-4 w-4 rounded border-gray-300 text-accent focus:ring-accent"
          />
          Subject active
        </label>
      ) : null}

      <div className="flex flex-wrap items-center gap-3 border-t border-gray-100 pt-6">
        <button
          type="submit"
          disabled={submitting || !canSubmit}
          className="inline-flex items-center justify-center gap-2 rounded-lg bg-gray-900 px-5 py-2.5 text-sm font-bold text-white shadow-sm transition-opacity hover:opacity-90 disabled:cursor-not-allowed disabled:opacity-45"
        >
          {submitting ? (
            <>
              <span className="material-symbols-outlined animate-spin text-[20px]">
                progress_activity
              </span>
              Saving…
            </>
          ) : (
            <>
              <span className="material-symbols-outlined text-[20px]">save</span>
              {mode === "create" ? "Create subject" : "Save changes"}
            </>
          )}
        </button>
        <button
          type="button"
          onClick={() => router.push("/dashboard/subjects")}
          className="rounded-lg border border-gray-200 px-4 py-2.5 text-sm font-semibold text-gray-600 hover:bg-gray-50"
        >
          Cancel
        </button>
      </div>
    </form>
  );
}
