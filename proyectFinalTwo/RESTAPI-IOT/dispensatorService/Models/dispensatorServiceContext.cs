using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace dispensatorService.Models
{
    public class dispensatorServiceContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public dispensatorServiceContext() : base("name=dispensatorServiceContext")
        {
        }

        public System.Data.Entity.DbSet<dispensatorService.Models.Configuration> Configurations { get; set; }
        public System.Data.Entity.DbSet<dispensatorService.Models.FoodDispenser> FoodDispenser { get; set; }
        public System.Data.Entity.DbSet<dispensatorService.Models.FoodHour> FoodHours { get; set; }
        public System.Data.Entity.DbSet<dispensatorService.Models.Log> Logs { get; set; }
        public System.Data.Entity.DbSet<dispensatorService.Models.WaterDispenser> WaterDispenser { get; set; }
    }
}