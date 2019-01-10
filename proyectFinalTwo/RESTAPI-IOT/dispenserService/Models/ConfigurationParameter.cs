using System;
using System.ComponentModel.DataAnnotations;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace dispenserService.Models
{
    public class ConfigurationParameter
    {
        [Key]
        public int ConfigurationId { get; set; }
        public double AmountDailyFood { get; set; }
        public double AmountBowlFoodWater { get; set; }
        public double AmountDailyWater { get; set; }

        [DataType(DataType.DateTime)]
        public DateTime CurrentDateTime { get; set; }
    }
}