import StatCard from "./StatCard";

export default function StatsOverview() {
  return (
    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
      <StatCard
        title="Projects"
        value="7,265"
        growth="+11.01%"
      />
      <StatCard
        title="Viewers"
        value="7,265"
        growth="+11.01%"
      />
      <StatCard
        title="Active Collaborators"
        value="7,265"
        growth="+11.01%"
      />
      <StatCard
        title="Enterprises"
        value="7,265"
        growth="+11.01%"
      />
    </section>
  );
}
