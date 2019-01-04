namespace dispenserService.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class segunda : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Configurations", "MilliLiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "MilliLiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "CurrentMilliLiterWater", c => c.Int(nullable: false));
            AddColumn("dbo.Logs", "MilliLiterWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "MilliLiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentMilliLiterWater", c => c.Int(nullable: false));
            AddColumn("dbo.WaterDispensers", "MilliLiterWaterDownloaded", c => c.Double(nullable: false));
            DropColumn("dbo.Configurations", "FoodPortion");
            DropColumn("dbo.Configurations", "LiterWater");
            DropColumn("dbo.Logs", "LiterWater");
            DropColumn("dbo.Logs", "CurrentPercentWater");
            DropColumn("dbo.Logs", "LiterWaterDownloaded");
            DropColumn("dbo.WaterDispensers", "LiterWater");
            DropColumn("dbo.WaterDispensers", "CurrentPercentWater");
            DropColumn("dbo.WaterDispensers", "LiterWaterDownloaded");
        }
        
        public override void Down()
        {
            AddColumn("dbo.WaterDispensers", "LiterWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.WaterDispensers", "CurrentPercentWater", c => c.Int(nullable: false));
            AddColumn("dbo.WaterDispensers", "LiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "LiterWaterDownloaded", c => c.Double(nullable: false));
            AddColumn("dbo.Logs", "CurrentPercentWater", c => c.Int(nullable: false));
            AddColumn("dbo.Logs", "LiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Configurations", "LiterWater", c => c.Double(nullable: false));
            AddColumn("dbo.Configurations", "FoodPortion", c => c.Double(nullable: false));
            DropColumn("dbo.WaterDispensers", "MilliLiterWaterDownloaded");
            DropColumn("dbo.WaterDispensers", "CurrentMilliLiterWater");
            DropColumn("dbo.WaterDispensers", "MilliLiterWater");
            DropColumn("dbo.Logs", "MilliLiterWaterDownloaded");
            DropColumn("dbo.Logs", "CurrentMilliLiterWater");
            DropColumn("dbo.Logs", "MilliLiterWater");
            DropColumn("dbo.Configurations", "MilliLiterWater");
        }
    }
}
