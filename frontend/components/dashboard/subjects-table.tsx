"use client";

import type { SubjectRow } from "@/lib/subjects-server";
import { activeBadgeClass } from "@/lib/subjects-server";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useMemo, useState } from "react";

type Props = {
  rows: SubjectRow[];
  total: number;
  fetchHint: string | null;
};

export function SubjectsTable({ rows, total, fetchHint }: Props) {
  const router = useRouter();
  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [deleting, setDeleting] = useState(false);
  const [deleteError, setDeleteError] = useState<string | null>(null);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return rows;
    return rows.filter((r) => r.name.toLowerCase().includes(q));
  }, [rows, query]);

  const selectedRow = selectedId
    ? rows.find((r) => r.id === selectedId) ?? null
    : null;

  async function handleDelete() {
    if (!selectedId) return;
    if (
      !window.confirm(
        `Delete subject "${selectedRow?.name ?? selectedId}"? This cannot be undone.`,
      )
    ) {
      return;
    }
    setDeleteError(null);
    setDeleting(true);
    try {
      const res = await fetch(`/api/subjects/${encodeURIComponent(selectedId)}`, {
        method: "DELETE",
      });
      if (!res.ok) {
        const data = (await res.json().catch(() => ({}))) as {
          error?: string;
          detail?: string;
        };
        setDeleteError(
          data.detail
            ? `${data.error ?? "Error"}: ${String(data.detail).slice(0, 400)}`
            : (data.error ?? `HTTP ${res.status}`),
        );
        return;
      }
      setSelectedId(null);
      router.refresh();
    } catch {
      setDeleteError("Network error while deleting.");
    } finally {
      setDeleting(false);
    }
  }

  return (
    <>
      {fetchHint ? (
        <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-950">
          {fetchHint}
        </div>
      ) : null}

      {deleteError ? (
        <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-900">
          {deleteError}
        </div>
      ) : null}

      <div className="mb-4 flex flex-col gap-3 rounded-xl border border-gray-100 bg-white p-4 sm:flex-row sm:flex-wrap sm:items-center sm:justify-between">
        <div className="flex w-full max-w-xl items-center rounded-lg border border-gray-200 bg-gray-50/80 px-3 py-2">
          <span className="material-symbols-outlined text-gray-400">search</span>
          <input
            type="search"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Filter by subject name"
            className="ml-2 w-full border-none bg-transparent text-sm placeholder:text-gray-400 focus:ring-0"
            aria-label="Filter subjects"
          />
        </div>
        <div className="flex flex-wrap items-center gap-2">
          {selectedId ? (
            <Link
              href={`/dashboard/subjects/${encodeURIComponent(selectedId)}`}
              className="inline-flex items-center justify-center gap-2 rounded-lg border border-gray-200 bg-white px-4 py-2 text-sm font-bold text-on-surface shadow-sm transition-colors hover:bg-gray-50"
            >
              <span className="material-symbols-outlined text-[20px]">edit</span>
              Edit
            </Link>
          ) : (
            <span className="inline-flex items-center justify-center gap-2 rounded-lg border border-gray-100 bg-gray-50 px-4 py-2 text-sm font-bold text-gray-400">
              <span className="material-symbols-outlined text-[20px]">edit</span>
              Edit
            </span>
          )}
          <button
            type="button"
            onClick={handleDelete}
            disabled={!selectedId || deleting}
            className="inline-flex items-center justify-center gap-2 rounded-lg border border-red-200 bg-red-50 px-4 py-2 text-sm font-bold text-red-900 transition-colors hover:bg-red-100 disabled:cursor-not-allowed disabled:opacity-45"
          >
            <span className="material-symbols-outlined text-[20px]">delete</span>
            {deleting ? "Deleting…" : "Delete"}
          </button>
        </div>
      </div>

      <div className="overflow-hidden rounded-xl border border-gray-100 bg-white">
        {filtered.length === 0 ? (
          <div className="flex flex-col items-center justify-center px-6 py-16 text-center">
            <span className="material-symbols-outlined mb-3 text-4xl text-gray-300">
              topic
            </span>
            <p className="text-sm font-semibold text-on-surface">
              {rows.length === 0
                ? "No subjects found."
                : "No rows match your filter."}
            </p>
            <p className="mt-1 max-w-md text-sm text-gray-500">
              {rows.length === 0
                ? "Create subjects to organize the service catalog for your tenant."
                : "Adjust your search or clear the field above."}
            </p>
            {rows.length === 0 ? (
              <Link
                href="/dashboard/subjects/new"
                className="mt-5 inline-flex items-center gap-2 rounded-lg bg-gray-900 px-4 py-2 text-sm font-bold text-white hover:opacity-90"
              >
                <span className="material-symbols-outlined text-[20px]">add</span>
                New subject
              </Link>
            ) : null}
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full min-w-[720px] text-left text-sm">
              <thead>
                <tr className="border-b border-gray-100 bg-gray-50/80 text-xs font-bold uppercase tracking-wider text-gray-400">
                  <th className="w-12 px-3 py-3 font-bold" aria-label="Select" />
                  <th className="px-5 py-3 font-bold">Name</th>
                  <th className="px-5 py-3 font-bold">Status</th>
                  <th className="px-5 py-3 font-bold">Web</th>
                  <th className="px-5 py-3 font-bold">App</th>
                  <th className="px-5 py-3 font-bold">Updated</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map((row) => {
                  const isSelected = selectedId === row.id;
                  return (
                    <tr
                      key={row.id}
                      onClick={() =>
                        setSelectedId((cur) => (cur === row.id ? null : row.id))
                      }
                      className={`cursor-pointer transition-colors ${
                        isSelected
                          ? "bg-blue-50/80 ring-1 ring-inset ring-blue-200"
                          : "hover:bg-gray-50/60"
                      }`}
                    >
                      <td className="px-3 py-4">
                        <input
                          type="radio"
                          name="subject-selected"
                          checked={isSelected}
                          onChange={() => setSelectedId(row.id)}
                          onClick={(e) => e.stopPropagation()}
                          className="h-4 w-4 border-gray-300 text-accent focus:ring-accent"
                          aria-label={`Select ${row.name}`}
                        />
                      </td>
                      <td className="max-w-xs px-5 py-4">
                        <span className="font-semibold text-on-surface">
                          {row.name}
                        </span>
                        <div className="mt-0.5 text-xs text-gray-400">
                          Created {row.createdLabel}
                        </div>
                      </td>
                      <td className="whitespace-nowrap px-5 py-4">
                        <span
                          className={`inline-flex rounded-full border px-2.5 py-0.5 text-xs font-bold ${activeBadgeClass(row.active)}`}
                        >
                          {row.active ? "Active" : "Inactive"}
                        </span>
                      </td>
                      <td className="whitespace-nowrap px-5 py-4 text-gray-600">
                        {row.visibleWeb ? "Yes" : "No"}
                      </td>
                      <td className="whitespace-nowrap px-5 py-4 text-gray-600">
                        {row.visibleApp ? "Yes" : "No"}
                      </td>
                      <td className="whitespace-nowrap px-5 py-4 text-gray-500">
                        {row.updatedLabel}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
        {rows.length > 0 ? (
          <div className="flex flex-col items-start justify-between gap-2 border-t border-gray-100 bg-gray-50/50 px-5 py-3 text-xs text-gray-500 sm:flex-row sm:items-center">
            <span>
              Showing{" "}
              <strong className="text-on-surface">{filtered.length}</strong>
              {query.trim() ? (
                <>
                  {" "}
                  filtered of{" "}
                  <strong className="text-on-surface">{rows.length}</strong>
                </>
              ) : (
                <>
                  {" "}
                  of <strong className="text-on-surface">{total}</strong> in the
                  tenant (current page)
                </>
              )}
            </span>
            <span className="text-gray-400">
              Click a row or the radio control to select a record.
            </span>
          </div>
        ) : null}
      </div>
    </>
  );
}
