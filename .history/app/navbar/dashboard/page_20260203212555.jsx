import WelcomeSection 
import StatsOverview from "./components/dashboard/StatsOverview";
import MyProjectsSection from "./components/dashboard/MyProjectSection";

export default function DashboardPage() {
  return (
    <div className="space-y-10">
      <WelcomeSection />
      <StatsOverview />
      <MyProjectsSection />
    </div>
  );
}
