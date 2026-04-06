"use client";

import type { ServiceRequestRow } from "@/lib/service-requests-server";
import { statusBadgeClass } from "@/lib/service-requests-server";
import Link from "next/link";
import { useMemo, useState } from "react";

type Props = {
  rows: ServiceRequestRow[];
  total: number;
  fetchHint: string | null;
};

export function ServiceRequestsTable({ rows, total, fetchHint }: Props) {
  const [query, setQuery] = useState("");

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return rows;
    return rows.filter((r) => {
      const desc = (r.description ?? "").toLowerCase();
      return (
        r.protocol.toLowerCase().includes(q) ||
        desc.includes(q) ||
        (r.channelLabel?.toLowerCase().includes(q) ?? false) ||
        (r.statusLabel?.toLowerCase().includes(q) ?? false)
      );
    });
  }, [rows, query]);

  return (
    <>
      {fetchHint ? (
        <div className="mb-4 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-950">
          {fetchHint}
        </div>
      ) : null}

      <div className="mb-4 flex flex-col gap-3 rounded-xl border border-gray-100 bg-white p-4 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex w-full max-w-xl items-center rounded-lg border border-gray-200 bg-gray-50/80 px-3 py-2">
          <span className="material-symbols-outlined text-gray-400">search</span>
          <input
            type="search"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Filter loaded rows (protocol, description…)"
            className="ml-2 w-full border-none bg-transparent text-sm placeholder:text-gray-400 focus:ring-0"
            aria-label="Filter service requests"
          />
        </div>
      </div>

      <div className="overflow-hidden rounded-xl border border-gray-100 bg-white">
        {filtered.length === 0 ? (
          <div className="flex flex-col items-center justify-center px-6 py-16 text-center">
            <span className="material-symbols-outlined mb-3 text-4xl text-gray-300">
              assignment_add
            </span>
            <p className="text-sm font-semibold text-on-surface">
              {rows.length === 0
                ? "No service requests found."
                : "No rows match your filter."}
            </p>
            <p className="mt-1 max-w-md text-sm text-gray-500">
              {rows.length === 0
                ? "When the API returns records for the configured tenant, they will appear here."
                : "Adjust your search or clear the field above."}
            </p>
            {rows.length === 0 ? (
              <Link
                href="/dashboard/service-requests/new"
                className="mt-5 inline-flex items-center gap-2 rounded-lg bg-gray-900 px-4 py-2 text-sm font-bold text-white hover:opacity-90"
              >
                <span className="material-symbols-outlined text-[20px]">add</span>
                New service request
              </Link>
            ) : null}
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full min-w-[720px] text-left text-sm">
              <thead>
                <tr className="border-b border-gray-100 bg-gray-50/80 text-xs font-bold uppercase tracking-wider text-gray-400">
                  <th className="px-5 py-3 font-bold">Protocol</th>
                  <th className="px-5 py-3 font-bold">Description</th>
                  <th className="px-5 py-3 font-bold">Channel</th>
                  <th className="px-5 py-3 font-bold">Status</th>
                  <th className="px-5 py-3 font-bold">Updated</th>
                  <th className="px-5 py-3 font-bold text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-50">
                {filtered.map((row) => (
                  <tr
                    key={row.id}
                    className="transition-colors hover:bg-gray-50/60"
                  >
                    <td className="whitespace-nowrap px-5 py-4">
                      <span className="font-mono text-sm font-bold text-on-surface">
                        {row.protocol}
                      </span>
                    </td>
                    <td className="max-w-xs px-5 py-4">
                      <span className="line-clamp-2 font-semibold text-on-surface">
                        {row.description ?? "—"}
                      </span>
                      <div className="mt-0.5 text-xs text-gray-400">
                        Opened {row.createdLabel}
                      </div>
                    </td>
                    <td className="whitespace-nowrap px-5 py-4 text-gray-600">
                      {row.channelLabel ?? "—"}
                    </td>
                    <td className="px-5 py-4">
                      {row.statusLabel ? (
                        <span
                          className={`inline-flex rounded-full border px-2.5 py-0.5 text-xs font-bold ${statusBadgeClass(row.statusCode)}`}
                        >
                          {row.statusLabel}
                        </span>
                      ) : (
                        "—"
                      )}
                    </td>
                    <td className="whitespace-nowrap px-5 py-4 text-gray-500">
                      {row.updatedLabel}
                    </td>
                    <td className="px-5 py-4 text-right">
                      <button
                        type="button"
                        className="inline-flex items-center gap-1 rounded-lg border border-gray-200 px-3 py-1.5 text-[11px] font-bold text-on-surface transition-colors hover:bg-white disabled:cursor-not-allowed disabled:opacity-45"
                        title="Details coming soon"
                        disabled
                      >
                        <span className="material-symbols-outlined text-[16px]">
                          visibility
                        </span>
                        Details
                      </button>
                    </td>
                  </tr>
                ))}
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
          </div>
        ) : null}
      </div>
    </>
  );
}
